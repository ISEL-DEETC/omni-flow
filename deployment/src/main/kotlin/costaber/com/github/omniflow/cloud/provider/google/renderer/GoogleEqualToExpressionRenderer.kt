package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.EqualToExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleEqualToExpressionRenderer(
    private val equalToExpression: EqualToExpression<*>,
) : GoogleRenderer() {

    override val element: Node = equalToExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("\${${equalToExpression.left.term()} == ${equalToExpression.right.term()}}")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}