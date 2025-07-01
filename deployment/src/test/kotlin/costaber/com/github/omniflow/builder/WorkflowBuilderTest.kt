package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.call
import costaber.com.github.omniflow.dsl.step
import costaber.com.github.omniflow.dsl.workflow
import costaber.com.github.omniflow.model.HttpMethod
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.util.*
import kotlin.test.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEqualTo

internal class WorkflowBuilderTest {

    @Test
    fun `workflow has expected result`() {
        val actualResult = workflow {
            name(WORKFLOW_NAME_1)
            description(WORKFLOW_DESCRIPTION_1)
            params(WORKFLOW_INPUT_1)
            steps(
                step {
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
                }
            )
            result(WORKFLOW_RESULT_1)
        }

        expectThat(actualResult)
            .isA<Workflow>()
            .and {
                get { name }.isEqualTo(WORKFLOW_NAME_1)
                get { input }.isEqualTo(WORKFLOW_INPUT_1)
                get { description }.isEqualTo(WORKFLOW_DESCRIPTION_1)
                get { steps }.containsExactly(STEP_1)
                get { result }.isEqualTo(WORKFLOW_RESULT_1)
            }
    }
}