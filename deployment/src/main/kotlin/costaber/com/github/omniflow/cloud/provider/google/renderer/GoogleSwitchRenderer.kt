package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.ConditionalContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleSwitchRenderer(private val conditionalContext: ConditionalContext) : GoogleRenderer() {

    override val element: Node = conditionalContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("switch:")
        }


    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        conditionalContext.default?.let {
            render(renderingContext) {
                add("next: $it")
            }
        }.orEmpty()
}