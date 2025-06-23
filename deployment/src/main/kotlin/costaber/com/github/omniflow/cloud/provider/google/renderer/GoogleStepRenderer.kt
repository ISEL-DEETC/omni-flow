package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleStepRenderer(private val step: Step) : IndentedNodeRenderer() {

    override val element: Node = step

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            if (step.next.isNotEmpty()) {
                addLine("- ${step.name}:")
                tab { add("next: ${step.next}") }
            } else {
                add("- ${step.name}:")
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext) = "" // nothing
}