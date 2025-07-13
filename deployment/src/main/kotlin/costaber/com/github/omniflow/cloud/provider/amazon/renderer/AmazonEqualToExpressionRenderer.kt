package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonEqualToExpressionRenderer(
    private val equalToExpression: EqualToExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonRenderer() {

    override val element: Node = equalToExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(
                "$AMAZON_VARIABLE\"\$.${
                    amazonTermResolver.resolveVariable(
                        equalToExpression.left,
                        Notation.DOT_NOTATION
                    )
                }\","
            )
            when (equalToExpression.right) {
                is Variable -> add(
                    "$AMAZON_STRING_EQUALS_PATH\"\$.${
                        amazonTermResolver.resolveVariable(
                            equalToExpression.right,
                            Notation.DOT_NOTATION
                        )
                    }\","
                )

                is Value<*> -> renderValue(equalToExpression.right)
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing

    private fun IndentedRenderingContext.renderValue(value: Value<*>) {
        when (value.value) {
            is Number -> add("$AMAZON_NUMERIC_EQUALS${value.term()},")
            is String -> add("$AMAZON_STRING_EQUALS\"${value.term()}\",")
            is Boolean -> add("$AMAZON_BOOLEAN_EQUALS${value.term()},")
        }
    }
}