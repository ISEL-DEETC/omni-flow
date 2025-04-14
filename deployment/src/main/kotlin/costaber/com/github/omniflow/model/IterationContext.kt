package costaber.com.github.omniflow.model

open class IterationContext(open val value: String, open val steps: List<Step>) : StepContext {
    override fun childNodes(): List<Node> {
        return steps
    }
}

data class IterationRangeContext(override val value: String, override val steps: List<Step>, val range: Range): IterationContext(value, steps)
data class IterationForEachContext(override val value: String, override val steps: List<Step>, val forEachVariable: Variable): IterationContext(value, steps)

