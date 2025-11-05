package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.VariableInitialization
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.TermContext


class GoogleRenderingContext(
    indentationLevel: Int = 0,
    stringBuilder: StringBuilder = StringBuilder(),
    termContext: TermContext = object : TermContext {},
) : IndentedRenderingContext(indentationLevel, stringBuilder, termContext) {
    companion object {
        private var innerRenderingContext: MutableList<GoogleRenderingContext> = mutableListOf()
    }

    private var variables: MutableList<VariableInitialization<*>> = mutableListOf()

    fun setVariables(variables: Collection<VariableInitialization<*>>) {
        this.variables.addAll(variables)
    }

    fun getVariables(): List<VariableInitialization<*>> = variables

    fun appendInnerRenderingContext(innerContext: GoogleRenderingContext) {
        innerRenderingContext.add(innerContext)
    }

    fun popLastRenderingContext(): GoogleRenderingContext {
        return innerRenderingContext.removeLastOrNull() ?: this
    }

    fun getLastRenderingContext(): GoogleRenderingContext {
        return innerRenderingContext.lastOrNull() ?: this
    }

    fun nestedLevel(): Int {
        return innerRenderingContext.size
    }

    override fun toString(): String {
        return "GoogleRenderingContext(indentationLevel=${getIndentationLevel()},variables=$variables,nestedLevel=${nestedLevel()})"
    }
}