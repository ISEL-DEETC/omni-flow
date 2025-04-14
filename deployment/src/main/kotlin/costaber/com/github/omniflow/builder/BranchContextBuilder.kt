package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.*

class BranchContextBuilder : Builder<StepContext> {

    private lateinit var name: String
    private var description: String? = null
    private val steps: MutableList<StepBuilder> = mutableListOf()

    fun name(name: String) = apply { this.name = name }

    fun description(description: String) = apply { this.description = description }

    fun steps(vararg value: StepBuilder) = apply { steps.addAll(value.toList()) }


    override fun build() : BranchContext {
        return BranchContext(name, description, steps.map { it.build() })
    }
}