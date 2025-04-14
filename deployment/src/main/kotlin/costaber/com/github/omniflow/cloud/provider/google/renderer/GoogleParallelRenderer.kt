package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.RenderingContext
import costaber.com.github.omniflow.renderer.TermContext
import costaber.com.github.omniflow.resource.util.render

class GoogleParallelRenderer(private val parallelContext: ParallelContext) : IndentedNodeRenderer() {

    override val element: Node = parallelContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            when(parallelContext) {
                is ParallelIterationContext ->
                    add("parallel:")
                is ParallelBranchContext -> {
                    addLine("parallel:")
                    tab {
                        add("branches:")
                    }
                }

            }

            incIndentationLevel()
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext) =
        render(renderingContext) {
            decIndentationLevel()
        }
}
