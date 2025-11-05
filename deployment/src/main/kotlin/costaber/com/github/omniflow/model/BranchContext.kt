package costaber.com.github.omniflow.model

data class BranchContext(
    val name: String,
    val description: String? = null,
    val steps: List<Step> = emptyList(),
) : StepContext {
    override fun childNodes(): List<Node> {
        return steps
    }
}
