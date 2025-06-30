package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.model.CallContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Term
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.TermContext
import costaber.com.github.omniflow.resource.util.render

class AmazonCallRenderer(
    private val callContext: CallContext,
    private val amazonTermResolver: AmazonTermResolver,
) : AmazonRenderer() {

    private val objectMapper = AmazonObjectMapper.default

    override val element: Node = callContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(AMAZON_TASK_TYPE)
            addLine(AMAZON_RESOURCE)
            addLine("$AMAZON_INPUT_PATH\"\$\",")
            addLine(AMAZON_START_PARAMETERS)
            tab {
                addLine("${AMAZON_API_ENDPOINT}\"${callContext.host}\",")
                addLine("${AMAZON_METHOD}\"${callContext.method}\",")
                add("${AMAZON_PATH}\"${callContext.path}\"")
                renderMap(AMAZON_HEADERS, callContext.header, renderingContext.termContext)
                renderMap(AMAZON_QUERY_PARAMETERS, callContext.query, renderingContext.termContext)
                renderBody()
                renderAuth()
                addEmptyLine()
            }
            add(AMAZON_CLOSE_OBJECT_WITH_COMMA)
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val nextStepName = amazonContext.getNextStepName()
        val currentStepName = amazonContext.getCurrentStepName()
        amazonTermResolver.addVariable(
            prefix = amazonContext.getCurrentStepName().orEmpty(),
            name = callContext.result,
        )
        return render(renderingContext) {
            addLine(AMAZON_START_RESULT_SELECTOR)
            tab {
                addLine("\"${callContext.result}.\$\": \"\$.${AMAZON_RESPONSE_BODY}\"")
            }
            addLine(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            addLine("$AMAZON_START_RESULT_PATH\"\$.${currentStepName}\",")
            if (nextStepName == null || nextStepName.isBlank()) {
                add(AMAZON_END)
            } else {
                add("${AMAZON_NEXT}\"${nextStepName}\"")
            }
        }
    }


    private fun IndentedRenderingContext.renderMap(
        title: String,
        mapToRender: Map<String, Term<*>>,
        termContext: TermContext,
    ) {
        if (mapToRender.isNotEmpty()) {
            append(",")
            addEmptyLine()
            addLine(title)
            tab {AMAZON_START_RESULT_PATH
                val mutableMap = mapToRender.toMutableMap()
                val last = mutableMap.entries.lastOrNull()
                last?.let { mutableMap.remove(last.key) }
                mutableMap.forEach {
                    addLine("${renderMapEntry(it, termContext)},")
                }
                last?.let { addLine(renderMapEntry(it, termContext)) }
            }
            add("}")
        }
    }

    private fun renderMapEntry(mapEntry: Map.Entry<String, Term<*>>, termContext: TermContext): String {
        var value = amazonTermResolver.resolve(mapEntry.value, termContext)
        if (mapEntry.value is Value) {
            value = "\"States.Array(States.Format('{}', $value))\""
        }
        return "\"${mapEntry.key}.\$\": $value"
    }

    private fun IndentedRenderingContext.renderBody() {
        callContext.body?.let {
            append(",")
            addEmptyLine()
            add(AMAZON_REQUEST_BODY)
            append("{")
            addEmptyLine()
            tab {
                processBody(it)
            }
            add("}")
        }
    }

    private fun IndentedRenderingContext.processBody(body: Any) {
        if (body is String) {
            addLine("$AMAZON_REQUEST_PAYLOAD \"$body\"")
        } else {
            val parsedBody = parseBodyIfMap(body)
            val lines = objectMapper.writeValueAsString(parsedBody)
                .split("{", "}", ",")
                .filterNot(String::isEmpty)
                .toMutableList()
            lines.removeFirstOrNull()?.let { line -> add(line) }
            lines.forEach { line ->
                append(",")
                addEmptyLine()
                add(line)
            }
            addEmptyLine()
        }
    }

    private fun parseBodyIfMap(body: Any): Any {
        if (body is Map<*, *>) {
            val resultMap: MutableMap<Any?, Any?> = body.toMutableMap()
            val stack: MutableList<MutableMap<Any?, Any?>> = mutableListOf()
            stack.add(resultMap)
            while (stack.isNotEmpty()) {
                val currentMap = stack.removeAt(0)
                currentMap.forEach {
                    if (it.value is Map<*, *>) {
                        stack.add((it.value as Map<*, *>).toMutableMap())
                    }
                    if (it.key is String && it.value is Variable) {
                        currentMap.remove(it.key)
                        currentMap["${it.key}.$"] = "$.${(it.value as Variable).name}"
                    }
                }
            }
            return resultMap
        }
        return body
    }

    private fun IndentedRenderingContext.renderAuth() {
        callContext.authentication?.let {
            append(",")
            addEmptyLine()
            add("${AMAZON_AUTH_TYPE}\"${it.type}\"")
        }
    }
}