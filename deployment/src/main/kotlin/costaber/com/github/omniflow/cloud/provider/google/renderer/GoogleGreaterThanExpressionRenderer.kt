package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.GreaterThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleGreaterThanExpressionRenderer(
    private val greaterThanExpression: GreaterThanExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = greaterThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(greaterThanExpression.left, Notation.DOT_NOTATION)
        val value = when (greaterThanExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(greaterThanExpression.right, Notation.DOT_NOTATION)
            else -> greaterThanExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key > ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}