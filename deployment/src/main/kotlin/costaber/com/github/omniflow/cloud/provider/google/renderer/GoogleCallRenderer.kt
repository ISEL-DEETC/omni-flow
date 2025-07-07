package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.builder.ResultType
import costaber.com.github.omniflow.cloud.provider.google.jackson.GoogleObjectMapper
import costaber.com.github.omniflow.model.CallContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Term
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleCallRenderer(
    private val callContext: CallContext,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {
    private val objectMapper = GoogleObjectMapper.default

    override val element: Node = callContext

    private fun ResultType.name(): String = when(this) {
        ResultType.BODY -> "body"
        ResultType.CODE -> "code"
        ResultType.HEADERS -> "headers"
    }

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            val googleTermContext = termContext as GoogleTermContext
            val httpMethod = callContext.method.name.lowercase()
            googleTermResolver.addVariableTranslation(
                name = callContext.result,
                translation = "${callContext.result}.${callContext.resultType.name()}"
            )
            addLine("call: http.${httpMethod}")
            addLine("args:")
            tab {
                add("url: ${callContext.host + callContext.path}")
            }
            renderMap("headers", callContext.header, googleTermContext)
            renderMap("query", callContext.query, googleTermContext)
            renderAuth()
            renderBody()
            renderTimeout()
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            val googleTermContext = termContext as GoogleTermContext
            googleTermContext.addResultVariable(callContext.result)
            add("result: ${callContext.result}")
        }

    private fun IndentedRenderingContext.renderMap(
        mapName: String,
        mapToRender: Map<String, Term<*>>,
        googleTermContext: GoogleTermContext,
    ) {
        if (mapToRender.isEmpty())
            return

        tab {
            addEmptyLine()
            add("$mapName:")
            mapToRender.forEach {
                addEmptyLine()
                val value = googleTermResolver.resolve(it.value, googleTermContext)
                tab {
                    add("${it.key}: $value")
                }
            }
        }
    }

    private fun IndentedRenderingContext.renderBody() {
        if (callContext.bodyRaw.isNotEmpty()) {
            tab {
                addEmptyLine()
                add("body: \"${callContext.bodyRaw}\"")
            }
        } else if (callContext.body.isNotEmpty()) {
            val yamlString = objectMapper.writeValueAsString(callContext.body)
                .split("\n")
                .filterNot(String::isEmpty)
            tab {
                addEmptyLine()
                add("body:")
                tab {
                    yamlString.forEach { line ->
                        addEmptyLine()
                        add(line)
                    }
                }
            }
        }
    }

    private fun IndentedRenderingContext.renderAuth() {
        callContext.authentication?.let {
            addEmptyLine()
            tab {
                addLine("auth:")
                tab {
                    add("type: ${it.type}")
                    it.scope?.let { scope ->
                        addEmptyLine()
                        add("scope: $scope")
                    }
                    it.scopes?.let { scopes ->
                        addEmptyLine()
                        add("scopes: $scopes")
                    }
                    it.audience?.let { audience ->
                        addEmptyLine()
                        add("audience: $audience")
                    }
                }
            }
        }
    }

    private fun IndentedRenderingContext.renderTimeout() {
        callContext.timeoutInSeconds?.let {
            addEmptyLine()
            tab {
                add("timeout: $it")
            }
        }
    }
}