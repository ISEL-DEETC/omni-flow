package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonParallelIterationRenderer(private val parallelIterationContext: ParallelIterationContext) : IndentedNodeRenderer() {

    override val element: Node = parallelIterationContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        val context = amazonContext.getLastRenderingContext()
        val innerContext = AmazonRenderingContext(context.getIndentationLevel() + 1)
        innerContext.setSteps(parallelIterationContext.iterationContext.steps)
        innerContext.getNextStepNameAndAdvance()
        innerContext.getNextStepNameAndAdvance()
        context.appendInnerRenderingContext(innerContext)

        if (parallelIterationContext.iterationContext !is IterationForEachContext)
            assert(false) { "This should never happen! AmazonTravesor makes all other ParallelIteration into ParallelBranchContext" }
        val iterationForEachContext: IterationForEachContext = parallelIterationContext.iterationContext as IterationForEachContext
        val innerName = "InnerMap${context.getCurrentStepName()}"

        return render(context) {
            addLine(AMAZON_MAP_TYPE)
            addLine("\"ItemsPath\": \"$.${iterationForEachContext.forEachVariable.name}\",")
            addLine("\"ItemSelector\": $AMAZON_OPEN_OBJECT")
            tab {
                addLine("\"${iterationForEachContext.value}.$\": \"$$.Map.Item.Value\"")
            }
            addLine(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            addLine("\"ItemProcessor\": $AMAZON_OPEN_OBJECT")
            incIndentationLevel()
            innerContext.incIndentationLevel()
            addLine("$AMAZON_START_AT\"$innerName\",")
            addLine(AMAZON_STATES)
            incIndentationLevel()
            innerContext.incIndentationLevel()
            add("\"$innerName\": $AMAZON_OPEN_OBJECT")
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
            addLine(AMAZON_CLOSE_OBJECT)
            decIndentationLevel()
            innerContext.decIndentationLevel()
            addLine(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            if (nextStepName == null) {
                add(AMAZON_END)
            } else {
                add("$AMAZON_NEXT\"${nextStepName}\"")
            }
        }
    }

}