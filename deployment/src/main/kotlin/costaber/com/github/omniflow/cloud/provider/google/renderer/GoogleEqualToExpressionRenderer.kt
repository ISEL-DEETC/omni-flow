package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.EqualToExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleEqualToExpressionRenderer(
    private val equalToExpression: EqualToExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = equalToExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(equalToExpression.left, Notation.DOT_NOTATION)
        val value = when(equalToExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(equalToExpression.right, Notation.DOT_NOTATION)
            else -> equalToExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key == ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}