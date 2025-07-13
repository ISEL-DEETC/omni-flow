package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.IterationContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonIterationRenderer(private val iterationContext: IterationContext) : IndentedNodeRenderer() {

    override val element: Node = iterationContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val context = amazonContext.getLastRenderingContext()
        val innerContext = AmazonRenderingContext(context.getIndentationLevel() + 1)
        innerContext.setSteps(iterationContext.steps)
        amazonContext.appendInnerRenderingContext(innerContext)
        return render(context) {
            addLine(AMAZON_PARALLEL_TYPE)
            addLine(AMAZON_START_BRANCHES)
            incIndentationLevel()
            innerContext.incIndentationLevel()
            addLine(AMAZON_OPEN_OBJECT)
            incIndentationLevel()
            innerContext.incIndentationLevel()
            addLine("${AMAZON_START_AT}\"${innerContext.getNextStepNameAndAdvance()}\",")
            add(AMAZON_STATES)
        }
    }


    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val nextStepName = amazonContext.getNextStepName()
        val innerContext = amazonContext.popLastRenderingContext()
        val context = amazonContext.getLastRenderingContext()
        return render(context) {
            addLine(AMAZON_CLOSE_OBJECT)
            decIndentationLevel()
            innerContext.decIndentationLevel()
            if (context.getNextStepNameAndAdvance() != null) {
                addLine(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            } else {
                addLine(AMAZON_CLOSE_OBJECT)
            }
            decIndentationLevel()
            innerContext.decIndentationLevel()
            addLine(AMAZON_CLOSE_ARRAY_WITH_COMMA)
            if (nextStepName == null) {
                add(AMAZON_END)
            } else {
                add("$AMAZON_NEXT\"${nextStepName}\"")
            }
        }
    }
}


