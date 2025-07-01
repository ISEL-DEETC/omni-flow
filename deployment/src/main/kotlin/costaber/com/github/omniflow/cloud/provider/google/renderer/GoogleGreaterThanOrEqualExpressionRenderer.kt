package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.GreaterThanOrEqualExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleGreaterThanOrEqualExpressionRenderer(
    private val greaterThanOrEqualExpression: GreaterThanOrEqualExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = greaterThanOrEqualExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(greaterThanOrEqualExpression.left, Notation.DOT_NOTATION)
        val value = when(greaterThanOrEqualExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(greaterThanOrEqualExpression.right, Notation.DOT_NOTATION)
            else -> greaterThanOrEqualExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key >= ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}