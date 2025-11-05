package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_COMMENT
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_START_AT
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_STATES
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonStateMachineRenderer(private val workflow: Workflow) : AmazonRenderer() {

    override val element: Node = workflow

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val context = renderingContext as AmazonRenderingContext
        context.setSteps(workflow.steps)
        return render(renderingContext) {
            addLine("{")
            tab {
                addLine("${AMAZON_COMMENT}\"${workflow.description}\",")
                addLine("${AMAZON_START_AT}\"${context.getNextStepNameAndAdvance()}\",")
                add(AMAZON_STATES)
            }
            incIndentationLevel()
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            decIndentationLevel()
            tab {
                addLine("}")
            }
            add("}")
        }
}