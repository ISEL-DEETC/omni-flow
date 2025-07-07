package costaber.com.github.omniflow.model

import costaber.com.github.omniflow.builder.ResultType

data class CallContext(
    val method: HttpMethod,
    val host: String,
    val path: String,
    val authentication: Authentication? = null,
    val body: Map<String, Any> = emptyMap(),
    val bodyRaw: String = "",
    val header: Map<String, Term<*>> = emptyMap(),
    val query: Map<String, Term<*>> = emptyMap(),
    val timeoutInSeconds: Long? = null,
    val result: String,
    val resultType: ResultType = ResultType.BODY,
) : StepContext {

    override fun childNodes() = emptyList<Node>()
}