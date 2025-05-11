package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.model.GreaterThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonGreaterThanExpressionRenderer(
    private val greaterThanExpression: GreaterThanExpression<*>,
) : AmazonRenderer() {

    override val element: Node = greaterThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine("$AMAZON_VARIABLE\"\$.${greaterThanExpression.left.term()}\",")
            when (greaterThanExpression.right) {
                is Value<*> -> add("\"NumericGreaterThan\": ")
                is Variable -> add("\"NumericGreaterThanPath\": ")
            }
            append("${greaterThanExpression.right.term()},")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}