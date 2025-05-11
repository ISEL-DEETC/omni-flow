package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import com.fasterxml.jackson.databind.ObjectMapper
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.VariableInitialization
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonVariablesResolver(
    private val variableInitialization: VariableInitialization<*>
) : AmazonRenderer() {

    private val objectMapper = ObjectMapper()

    override val element: Node = variableInitialization

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = renderingContext as AmazonRenderingContext
        return render(renderingContext) {
            add("\"${variableInitialization.variable.name}\": ${objectMapper.writeValueAsString(variableInitialization.term.term())}")
            if (amazonContext.isNotLastVariable(variableInitialization)) {
                append(",")
            }
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String = "" // nothing
}