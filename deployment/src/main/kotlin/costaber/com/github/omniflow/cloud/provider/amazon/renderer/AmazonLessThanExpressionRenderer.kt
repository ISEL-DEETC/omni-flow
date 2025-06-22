package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import com.fasterxml.jackson.databind.ObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.model.LessThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonLessThanExpressionRenderer(
    private val lessThanExpression: LessThanExpression<*>
) : AmazonRenderer() {
    private val objectMapper = ObjectMapper()

    override val element: Node = lessThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine("$AMAZON_VARIABLE\"\$.${lessThanExpression.left.term()}\",")
            when (lessThanExpression.right) {
                is Value<*> -> add("\"NumericLessThan\": ")
                is Variable -> add("\"NumericLessThanPath\": ")
            }
            val term = when (lessThanExpression.right) {
                is Variable -> "\"$.${lessThanExpression.right.term()}\""
                else -> objectMapper.writeValueAsString(lessThanExpression.right.term())
            }
            append("${term},")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}