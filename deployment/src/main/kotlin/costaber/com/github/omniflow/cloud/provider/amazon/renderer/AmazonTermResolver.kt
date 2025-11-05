package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.TermContext
import costaber.com.github.omniflow.renderer.TermResolver

object AmazonTermResolver : TermResolver() {

    override fun resolveVariable(variable: Variable, termContext: TermContext): String {
        val variableName = translateVariable(variable.name)
        val termDefinition = if (variableName.isNotEmpty()) "\$.${variableName}" else "\$"
        return "\"States.Array(States.Format('{}', ${termDefinition}))\""
    }

}