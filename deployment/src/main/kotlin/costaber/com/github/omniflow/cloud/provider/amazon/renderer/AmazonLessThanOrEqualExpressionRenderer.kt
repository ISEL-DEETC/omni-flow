package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.model.LessThanOrEqualExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonLessThanOrEqualExpressionRenderer(
    private val lessThanOrEqualExpression: LessThanOrEqualExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonRenderer() {
    private val objectMapper = AmazonObjectMapper.default

    override val element: Node = lessThanOrEqualExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine("$AMAZON_VARIABLE\"\$.${lessThanOrEqualExpression.left.term()}\",")
            when (lessThanOrEqualExpression.right) {
                is Value<*> -> add("\"NumericLessThanEquals\": ")
                is Variable -> add("\"NumericLessThanEqualsPath\": ")
            }
            val term = when (lessThanOrEqualExpression.right) {
                is Variable -> "\"$.${lessThanOrEqualExpression.right.term()}\""
                else -> objectMapper.writeValueAsString(lessThanOrEqualExpression.right.term())
            }
            append("${term},")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}