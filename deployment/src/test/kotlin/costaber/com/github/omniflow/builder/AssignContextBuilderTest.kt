package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.assign
import costaber.com.github.omniflow.dsl.value
import costaber.com.github.omniflow.dsl.variable
import costaber.com.github.omniflow.model.AssignContext
import costaber.com.github.omniflow.model.StepType
import costaber.com.github.omniflow.util.VARIABLE_INITIALIZATION_1
import costaber.com.github.omniflow.util.VARIABLE_INITIALIZATION_3
import costaber.com.github.omniflow.util.VARIABLE_NAME_1
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEqualTo

internal class AssignContextBuilderTest {

    @Test
    fun `assign has expected result`() {
        val assignBuilder = assign {
            variables(
                variable(VARIABLE_NAME_1) equalTo value("Mr.Robot"),
                variable(VARIABLE_NAME_1) equalTo value(999)
            )
        }

        expectThat(assignBuilder.stepType()).isEqualTo(StepType.ASSIGN)

        val actualResult = assignBuilder.build()

        expectThat(actualResult)
            .isA<AssignContext>()
            .and {
                get { variables }.containsExactly(
                    VARIABLE_INITIALIZATION_1, VARIABLE_INITIALIZATION_3
                )
            }
    }
}