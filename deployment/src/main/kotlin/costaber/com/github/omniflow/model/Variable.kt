package costaber.com.github.omniflow.model

data class Variable(
    val name: String,
    val withKey: String = "",
) : Term<String> {

    constructor(name: String) : this(name, "")

    override fun term() = name

    fun withKey(key: String): Variable {
        return this.copy(withKey = key)
    }
}