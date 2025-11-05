package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.resource.TAB

open class IndentedRenderingContext(
    private var indentationLevel: Int = 0,
    val stringBuilder: StringBuilder = StringBuilder(),
    val termContext: TermContext,
) : RenderingContext {

    private fun getIndentationString(): String =
        TAB.repeat(indentationLevel)

    fun incIndentationLevel() {
        indentationLevel += 1
    }

    fun decIndentationLevel() {
        indentationLevel -= 1
        if (indentationLevel < 0) {
            throw IllegalStateException("Indentation level cannot be negative")
        }
    }

    fun addLine(value: String) {
        stringBuilder.appendLine("${getIndentationString()}$value")
    }

    fun addEmptyLine() {
        stringBuilder.appendLine()
    }

    fun add(value: String) {
        stringBuilder.append("${getIndentationString()}$value")
    }

    fun append(value: String) {
        stringBuilder.append(value)
    }

    fun tab(block: IndentedRenderingContext.() -> Unit) {
        incIndentationLevel()
        block()
        decIndentationLevel()
    }

    fun getString() = stringBuilder.toString().also {
        stringBuilder.clear()
    }

    fun getIndentationLevel() = indentationLevel

    override fun toString(): String {
        return "IndentedRenderingContext(indentationLevel=$indentationLevel, termContext=$termContext)"
    }

}