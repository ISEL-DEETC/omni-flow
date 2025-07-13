package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.NotEqualToExpression
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleNotEqualToExpressionRenderer(
    private val notEqualToExpression: NotEqualToExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = notEqualToExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(notEqualToExpression.left, Notation.DOT_NOTATION)
        val value = when (notEqualToExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(notEqualToExpression.right, Notation.DOT_NOTATION)
            else -> notEqualToExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key != ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}