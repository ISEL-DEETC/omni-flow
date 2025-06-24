package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.GreaterThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleGreaterThanExpressionRenderer(
    private val greaterThanExpression: GreaterThanExpression<*>,
) : GoogleRenderer() {

    override val element: Node = greaterThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("\${${greaterThanExpression.left.term()} > ${greaterThanExpression.right.term()}}")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}