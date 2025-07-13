package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.LessThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleLessThanExpressionRenderer(
    private val lessThanExpression: LessThanExpression<*>,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = lessThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val key = googleTermResolver.resolveVariable(lessThanExpression.left, Notation.DOT_NOTATION)
        val value = when (lessThanExpression.right) {
            is Variable -> googleTermResolver.resolveVariable(lessThanExpression.right, Notation.DOT_NOTATION)
            else -> lessThanExpression.right.term()
        }
        return render(renderingContext) {
            add("\${$key < ${value}}")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}