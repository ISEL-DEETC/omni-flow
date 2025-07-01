package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.LessThanOrEqualExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleLessThanOrEqualExpressionRenderer(
    private val lessThanOrEqualExpression: LessThanOrEqualExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = lessThanOrEqualExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(lessThanOrEqualExpression.left, Notation.DOT_NOTATION)
        val value = when(lessThanOrEqualExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(lessThanOrEqualExpression.right, Notation.DOT_NOTATION)
            else -> lessThanOrEqualExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key <= ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}