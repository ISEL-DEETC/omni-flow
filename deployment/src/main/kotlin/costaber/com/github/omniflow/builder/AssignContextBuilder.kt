package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.*

class AssignContextBuilder : ContextBuilder {

    private val variables: MutableList<Pair<Variable, Any>> = mutableListOf()

    fun variables(vararg value: Pair<Variable, Any>) = apply { this.variables.addAll(value) }

    @Deprecated("Use infix function instead", ReplaceWith("Variable equalTo Term<T>"))
    infix fun String.equal(value: Any) = Pair(Variable(this), value)

    infix fun <T> Variable.equalTo(rightTerm: Term<T>): Pair<Variable, Term<T>> = Pair(
        first = this,
        second = rightTerm,
    )

    override fun stepType() = StepType.ASSIGN

    override fun build() = AssignContext(
        variables = variables.map {
            // This allows any Term<T> to be used in term without
            // boxing it with a Value<T> type.
            // Example having Variable instead of Value<Variable>
            // Since both are Term there's no need to box one inside the other
            @Suppress("UNCHECKED_CAST")
            VariableInitialization(
                variable = it.first,
                term = when (val second = it.second) {
                    is Term<*> -> second as Term<Any>
                    else -> Value(second)
                }
            )
        }
    )
}