package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.BranchContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonBranchRenderer(private val branchContext: BranchContext) : IndentedNodeRenderer() {

    override val element: Node = branchContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val context = amazonContext.getLastRenderingContext()
        val innerContext = AmazonRenderingContext(context.getIndentationLevel() + 2)
        innerContext.setSteps(branchContext.steps)
        innerContext.getNextStepNameAndAdvance()
        amazonContext.appendInnerRenderingContext(innerContext)
        return render(context) {
            addLine(AMAZON_OPEN_OBJECT)
            incIndentationLevel()
            addLine("${AMAZON_START_AT}\"${branchContext.steps.first().name}\",")
            add(AMAZON_STATES)
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        amazonContext.popLastRenderingContext()
        val context = amazonContext.getLastRenderingContext()
        return render(context) {
            addLine(AMAZON_CLOSE_OBJECT)
            decIndentationLevel()
            if (context.getNextStepNameAndAdvance() != null) {
                add(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            } else {
                add(AMAZON_CLOSE_OBJECT)
            }

        }
    }
}