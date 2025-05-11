package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.*

class AssignContextBuilder : ContextBuilder {

    private val variables: MutableList<Pair<String, Any>> = mutableListOf()

    fun variable(vararg value: Pair<String, Any>) = apply { this.variables.addAll(value) }

    infix fun String.equal(value: Any) = Pair(this, value)

    override fun stepType() = StepType.ASSIGN

    override fun build() = AssignContext(
        variables = variables.map {
            // This allows any Term<T> to be used in term without
            // boxing it with a Value<T> type.
            // Example having Variable instead of Value<Variable>
            // Since both are Term there's no need to box one inside the other
            @Suppress("UNCHECKED_CAST")
            VariableInitialization(
                variable = Variable(it.first),
                term = when (val second = it.second) {
                    is Term<*> -> second as Term<Any>
                    else -> Value(second)
                }
            )
        }
    )
}