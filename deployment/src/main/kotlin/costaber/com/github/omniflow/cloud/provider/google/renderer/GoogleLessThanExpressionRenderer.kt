package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.LessThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleLessThanExpressionRenderer(
    private val lessThanExpression: LessThanExpression<*>
) : GoogleRenderer() {

    override val element: Node = lessThanExpression

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("\${${lessThanExpression.left.term()} == ${lessThanExpression.right.term()}}")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}