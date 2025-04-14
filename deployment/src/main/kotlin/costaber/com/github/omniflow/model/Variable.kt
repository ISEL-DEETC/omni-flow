package costaber.com.github.omniflow.model

data class Variable(
    val name: String
) : Term<String> {

    override fun term() = name

    operator fun plus(variable: Variable): Any {
        TODO("Not yet implemented")
    }
}