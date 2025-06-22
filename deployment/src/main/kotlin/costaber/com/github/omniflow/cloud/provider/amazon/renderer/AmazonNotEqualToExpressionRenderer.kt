package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import com.fasterxml.jackson.databind.ObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.NotEqualToExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonNotEqualToExpressionRenderer(
    private val notEqualToExpression: NotEqualToExpression<*>,
) : AmazonRenderer() {
    private val objectMapper = ObjectMapper()
    override val element: Node = notEqualToExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(AMAZON_NOT)
            tab {
                addLine("$AMAZON_VARIABLE\"${notEqualToExpression.left.term()}\",")

                when (notEqualToExpression.right) {
                    is Variable -> add("$AMAZON_STRING_EQUALS_PATH\"\$.${notEqualToExpression.right.term()}\",")
                    is Value<*> -> renderValue(notEqualToExpression.right)
                }

                val term = when (notEqualToExpression.right) {
                    is Variable -> "\"$.${notEqualToExpression.right.term()}\""
                    else -> objectMapper.writeValueAsString(notEqualToExpression.right.term())
                }
                append("${term},")
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add(AMAZON_CLOSE_OBJECT_WITH_COMMA)
        }

    private fun IndentedRenderingContext.renderValue(value: Value<*>) {
        when (value.value) {
            is Number -> add(AMAZON_NUMERIC_EQUALS)
            is String -> add(AMAZON_STRING_EQUALS)
            is Boolean -> add(AMAZON_BOOLEAN_EQUALS)
        }
    }
}