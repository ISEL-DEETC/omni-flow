package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.cloud.provider.google.VARIABLE_DELIMITER
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.renderer.TermContext
import costaber.com.github.omniflow.renderer.TermResolver

object GoogleTermResolver : TermResolver() {

    override fun resolveVariable(variable: Variable, termContext: TermContext): String {
        val googleTermContext = termContext as GoogleTermContext
        val variableName = if (googleTermContext.isResultVariable(variable)) {
            variable.name.split(VARIABLE_DELIMITER)
                .toMutableList()
                .also { it.add(1, "body") }
                .joinToString(VARIABLE_DELIMITER)
        } else variable.name
        return "\${${variableName}}"
    }
}