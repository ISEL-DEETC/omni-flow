package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.condition
import costaber.com.github.omniflow.dsl.switch
import costaber.com.github.omniflow.model.ConditionalContext
import costaber.com.github.omniflow.model.StepType
import costaber.com.github.omniflow.util.CONDITION_1
import costaber.com.github.omniflow.util.CONDITION_CONTEXT_DEFAULT_1
import costaber.com.github.omniflow.util.EQUAL_TO_EXPRESSION_1
import costaber.com.github.omniflow.util.STEP_NAME_1
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class SwitchContextBuilderTest {

    @Test
    fun `switch has expected result`() {
        val switchBuilder = switch {
            conditions(
                condition {
                    match(EQUAL_TO_EXPRESSION_1)
                    jump(STEP_NAME_1)
                }
            )
            default(CONDITION_CONTEXT_DEFAULT_1)
        }

        expectThat(switchBuilder.stepType()).isEqualTo(StepType.CONDITIONAL)

        val actualResult = switchBuilder.build()

        expectThat(actualResult)
            .isA<ConditionalContext>()
            .and {
                get { conditions }.containsExactly(CONDITION_1)
                get { default }.isEqualTo(CONDITION_CONTEXT_DEFAULT_1)
            }
    }
}