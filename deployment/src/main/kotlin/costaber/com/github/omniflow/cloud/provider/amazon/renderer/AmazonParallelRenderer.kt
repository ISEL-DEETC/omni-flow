package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonParallelRenderer(private val parallelBranchContext: ParallelBranchContext) : IndentedNodeRenderer() {

    override val element: Node = parallelBranchContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val innerContext = AmazonRenderingContext(amazonContext.getIndentationLevel() + 1)
        innerContext.setSteps(parallelBranchContext.branches)
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