package costaber.com.github.omniflow.model

interface ParallelContext : StepContext

data class ParallelBranchContext(val branches: List<BranchContext>) : ParallelContext {
    override fun childNodes(): List<Node> {
        return branches
    }
}

data class ParallelIterationContext(val iterationContext: IterationContext) : ParallelContext {
    override fun childNodes(): List<Node> {
        return listOf(iterationContext)
    }
}