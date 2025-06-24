package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleParallelRenderer(private val parallelContext: ParallelContext) : IndentedNodeRenderer() {

    override val element: Node = parallelContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val currentContext = (renderingContext as GoogleRenderingContext).getLastRenderingContext()
        val innerContext = GoogleRenderingContext(
            indentationLevel = currentContext.getIndentationLevel() + 1,
            termContext = currentContext.termContext
        )
        currentContext.appendInnerRenderingContext(innerContext)
        return render(currentContext) {
            when (parallelContext) {
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
            innerContext.incIndentationLevel()
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val googleRenderingContext = (renderingContext as GoogleRenderingContext)
        val innerContext = googleRenderingContext.popLastRenderingContext()
        val currentContext = googleRenderingContext.getLastRenderingContext()
        val sharedVariables = currentContext.getVariables().map { it.variable.name }.toSet().intersect(innerContext.getVariables().map { it.variable.name }.toSet())
        return render(currentContext) {
            if (sharedVariables.isNotEmpty()) {
                tab { add("shared: [${sharedVariables.joinToString(",")}]") }
            }
            decIndentationLevel()
            innerContext.decIndentationLevel()
        }
    }
}
