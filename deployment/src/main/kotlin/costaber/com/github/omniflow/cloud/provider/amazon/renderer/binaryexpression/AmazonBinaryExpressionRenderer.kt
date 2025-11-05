package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_VARIABLE
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.BinaryExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

abstract class AmazonBinaryExpressionRenderer (
    private val binaryExpression: BinaryExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonRenderer() {

    override val element: Node = binaryExpression

    abstract val amazonVariablePath: String

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(
                "$AMAZON_VARIABLE\"\$.${
                    amazonTermResolver.resolveVariable(
                        binaryExpression.left,
                        Notation.DOT_NOTATION
                    )
                }\","
            )
            when (binaryExpression.right) {
                is Variable -> add(
                    "$amazonVariablePath\"\$.${
                        amazonTermResolver.resolveVariable(
                            binaryExpression.right,
                            Notation.DOT_NOTATION
                        )
                    }\","
                )

                is Value<*> -> renderValue(binaryExpression.right)
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing

    protected abstract fun IndentedRenderingContext.renderValue(value: Value<*>)
}
