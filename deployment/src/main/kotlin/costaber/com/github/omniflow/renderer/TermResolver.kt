package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.model.Notation
import costaber.com.github.omniflow.model.Term
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable

abstract class TermResolver {

    private val variableTranslator = mutableMapOf<String, String>()

    abstract fun resolveVariable(variable: Variable, termContext: TermContext): String

    fun addVariableTranslation(name: String, translation: String) {
        variableTranslator[name] = translation
    }

    fun translateVariable(variableName: String): String =
        variableTranslator[variableName] ?: variableName

    fun resolve(term: Term<*>, context: TermContext): String = when (term) {
        is Variable -> resolveVariable(term, context)
        is Value -> when (term.value) {
            is String -> "'${term.value}'"
            else -> term.value.toString()
        }
    }

    fun resolveVariable(variable: Variable, notation: Notation): String {
        if (variable.getWithKeys().isEmpty()) {
            return translateVariable(variable.term())
        }
        return when (notation) {
            Notation.DOT_NOTATION ->
                "${translateVariable(variable.term())}.${variable.getWithKeys().joinToString(".")}"
            Notation.SQUARE_BRACKETS_NOTATION ->
                "${variable.name}${variable.getWithKeys().joinToString("") { "[$it]" }}"
        }
    }
}