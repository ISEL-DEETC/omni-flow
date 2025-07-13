package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_EQUALS_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonGreaterThanOrEqualExpressionRenderer(
    private val greaterThanOrEqualExpression: GreaterThanOrEqualExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonRenderer() {

    override val element: Node = greaterThanOrEqualExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(
                "$AMAZON_VARIABLE\"\$.${
                    amazonTermResolver.resolveVariable(
                        greaterThanOrEqualExpression.left,
                        Notation.DOT_NOTATION
                    )
                }\","
            )
            when (greaterThanOrEqualExpression.right) {
                is Value<*> -> add("$AMAZON_NUMERIC_GREATER_THAN_EQUALS{greaterThanExpression.right.term()},")
                is Variable -> add(
                    "$AMAZON_NUMERIC_GREATER_THAN_EQUALS_PATH\"\$.${
                        amazonTermResolver.resolveVariable(
                            greaterThanOrEqualExpression.right,
                            Notation.DOT_NOTATION
                        )
                    }\",")
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}