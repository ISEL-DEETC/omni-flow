package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.AssignContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleAssignRenderer(assignContext: AssignContext) : GoogleRenderer() {

    override val element: Node = assignContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("assign:")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext) = "" // nothing
}