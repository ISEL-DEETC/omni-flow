package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonGreaterThanExpressionRenderer(
    private val greaterThanExpression: GreaterThanExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonRenderer() {

    override val element: Node = greaterThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(
                "$AMAZON_VARIABLE\"\$.${
                    amazonTermResolver.resolveVariable(
                        greaterThanExpression.left,
                        Notation.DOT_NOTATION
                    )
                }\","
            )
            when (greaterThanExpression.right) {
                is Value<*> -> add("$AMAZON_NUMERIC_GREATER_THAN${greaterThanExpression.right.term()},")
                is Variable -> add(
                    "$AMAZON_NUMERIC_GREATER_THAN_PATH\"\$.${
                        amazonTermResolver.resolveVariable(
                            greaterThanExpression.right,
                            Notation.DOT_NOTATION
                        )
                    }\",")
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}