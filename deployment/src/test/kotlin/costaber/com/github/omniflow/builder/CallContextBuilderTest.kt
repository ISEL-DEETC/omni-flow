package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.call
import costaber.com.github.omniflow.dsl.value
import costaber.com.github.omniflow.model.CallContext
import costaber.com.github.omniflow.model.HttpMethod
import costaber.com.github.omniflow.util.*
import strikt.api.expectThat
import strikt.assertions.containsKey
import strikt.assertions.containsKeys
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class CallContextBuilderTest {

    @Test
    fun `call has expected result`() {
        val actualResult = call {
            method(HttpMethod.GET)
            host(HOST)
            path(PATH)
            header(
                HEADER_CONTENT_TYPE to value(CONTENT_TYPE_APPLICATION_JSON),
                HEADER_ACCEPT to value(CONTENT_TYPE_APPLICATION_JSON),
            )
            query(
                QUERY_STRING_WORKFLOW_ID to value("123"),
            )
            body(BODY_1)
            authentication(
                costaber.com.github.omniflow.dsl.authentication {
                    type(AUTH_TYPE_2)
                }
            )
            timeout(TIMEOUT)
            result(CALL_CONTEXT_RESULT_1)
        }.build()

        expectThat(actualResult)
            .isA<CallContext>()
            .and {
                get { method }.isEqualTo(HttpMethod.GET)
                get { host }.isEqualTo(HOST)
                get { path }.isEqualTo(PATH)
                get { header }.containsKeys(HEADER_CONTENT_TYPE, HEADER_ACCEPT)
                get { query }.containsKey(QUERY_STRING_WORKFLOW_ID)
                get { bodyRaw }.isEqualTo(BODY_1)
                get { authentication?.type }.isEqualTo(AUTH_TYPE_2)
                get { timeoutInSeconds }.isEqualTo(TIMEOUT)
                get { result }.isEqualTo(CALL_CONTEXT_RESULT_1)
            }
    }
}