package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.Condition
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleConditionRenderer(private val condition: Condition) : GoogleRenderer() {

    override val element: Node = condition

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("- condition:")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            add("  next: ${condition.jump}")
        }
}