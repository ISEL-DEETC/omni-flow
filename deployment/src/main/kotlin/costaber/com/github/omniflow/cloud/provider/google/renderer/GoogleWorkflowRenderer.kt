package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleWorkflowRenderer(
    private val workflow: Workflow,
    private val googleTermResolver: GoogleTermResolver,
) : GoogleRenderer() {

    override val element: Node = workflow

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine("main:")
            incIndentationLevel()
            renderInput()
            add("steps:")
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            tab {
                addLine("- return_output:")
                tab {
                    val googleTermContext = termContext as GoogleTermContext
                    val variable = googleTermResolver.resolve(Variable(workflow.result), googleTermContext)
                    add("return: $variable")
                }
            }
        }.apply { renderingContext.decIndentationLevel() }

    private fun IndentedRenderingContext.renderInput() {
        workflow.input?.let {
            addLine("params: [ ${workflow.input} ]")
        }
    }
}