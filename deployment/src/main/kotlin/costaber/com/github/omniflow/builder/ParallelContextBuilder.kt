package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.*

class ParallelContextBuilder : ContextBuilder {

    private val branches: MutableList<BranchContextBuilder> = mutableListOf()
    private var iteration: IterationContextBuilder? = null
    override fun stepType() = StepType.PARALLEL

    fun iteration(iterationContextBuilder: IterationContextBuilder.() -> Unit) = apply {
        if (branches.isNotEmpty())
            throw IllegalArgumentException("Cannot use both Branches and Iteration at the same time.")
        iteration = IterationContextBuilder().apply(iterationContextBuilder)
    }

    fun branches(vararg value: BranchContextBuilder) = apply {
        if (iteration != null)
            throw IllegalArgumentException("Cannot use both Branches and Iteration at the same time.")
        this.branches.addAll(value)
    }

    override fun build(): ParallelContext {
        if (branches.isNotEmpty())
            return ParallelBranchContext(branches.map { it.build() })
        if (iteration != null)
            return ParallelIterationContext(iteration!!.build())
        throw IllegalStateException("Either Branches or Iteration need to be set.")
    }
}