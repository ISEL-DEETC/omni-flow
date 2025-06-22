package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.*

class IterationContextBuilder : ContextBuilder {

    private lateinit var key: String
    private var range: Range? = null
    private var forEachVariable: Variable? = null
    private val steps: MutableList<StepBuilder> = mutableListOf()

    override fun stepType() = StepType.ITERATION

    fun key(value: String) = apply { this.key = value }

    fun range(min: Int, max: Int) = apply {
        if (forEachVariable != null) {
            throw IllegalArgumentException("Cannot use both ForEach and Range at the same time.")
        }
        range = Range(min, max) }

    fun forEach(variable: Variable) = apply {
        if (range != null) {
            throw IllegalArgumentException("Cannot use both ForEach and Range at the same time.")
        }
        forEachVariable = variable
    }

    fun steps(vararg value: StepBuilder) = apply { steps.addAll(value.toList()) }


    override fun build() : IterationContext {
        if (range != null)
            return IterationRangeContext(key, steps.map { it.build() }, range!!)
        if (forEachVariable != null)
            return IterationForEachContext(key, steps.map { it.build() }, forEachVariable!!)
        throw IllegalStateException("Either ForEach or Range need to be set.")
    }
}