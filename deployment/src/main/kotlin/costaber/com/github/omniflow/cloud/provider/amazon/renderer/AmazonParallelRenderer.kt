package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.ParallelBranchContext
import costaber.com.github.omniflow.model.ParallelContext
import costaber.com.github.omniflow.model.ParallelIterationContext
import costaber.com.github.omniflow.model.StepContext
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonParallelRenderer(private val parallelContext: ParallelContext) : IndentedNodeRenderer() {

    override val element: Node = parallelContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val innerContext = AmazonRenderingContext(amazonContext.getIndentationLevel() + 1)
        when(parallelContext) {
            is ParallelBranchContext -> innerContext.setSteps(parallelContext.branches)
            is ParallelIterationContext -> innerContext.setSteps(parallelContext.iterationContext.steps)
        }
        innerContext.getNextStepNameAndAdvance()
        amazonContext.appendInnerRenderingContext(innerContext)
        return render(amazonContext) {
            addLine(AMAZON_PARALLEL_TYPE)
            add(AMAZON_START_BRANCHES)
        }
    }


    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val nextStepName = amazonContext.getNextStepName()
        amazonContext.popLastRenderingContext()
        return render(amazonContext) {
            addLine(AMAZON_CLOSE_ARRAY_WITH_COMMA)
            if (nextStepName == null) {
                add(AMAZON_END)
            } else {
                add("$AMAZON_NEXT\"${nextStepName}\"")
            }
        }
    }
}