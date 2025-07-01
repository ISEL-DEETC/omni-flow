package costaber.com.github.omniflow.model

data class Variable(
    val name: String
) : Term<String> {

    private val withKeys: MutableList<String> = mutableListOf()

    fun getWithKeys(): List<String> = withKeys

    override fun term() = name

    fun withKey(key: String): Variable {
        withKeys.add(key)
        return this
    }
}