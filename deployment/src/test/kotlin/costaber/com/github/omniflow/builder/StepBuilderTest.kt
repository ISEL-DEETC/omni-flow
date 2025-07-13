package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.call
import costaber.com.github.omniflow.dsl.step
import costaber.com.github.omniflow.model.HttpMethod
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.util.*
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class StepBuilderTest {

    @Test
    fun `step has expected result`() {
        val actualResult = step {
            name(STEP_NAME_1)
            description(STEP_DESCRIPTION_1)
            context(
                call {
                    method(HttpMethod.GET)
                    host(HOST)
                    path(PATH)
                    timeout(TIMEOUT)
                    result(CALL_CONTEXT_RESULT_1)
                }
            )
        }.build()

        expectThat(actualResult)
            .isA<Step>()
            .and {
                get { name }.isEqualTo(STEP_NAME_1)
                get { description }.isEqualTo(STEP_DESCRIPTION_1)
                get { context }.isEqualTo(CALL_CONTEXT_1)
            }
    }
}