package costaber.com.github.omniflow.dsl

import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.util.VALUE_1
import costaber.com.github.omniflow.util.VARIABLE_1
import costaber.com.github.omniflow.util.VARIABLE_NAME_1
import kotlin.test.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

internal class DslDefinitionTest {

    @Test
    fun `variable has expected result`() {
        val actualResult = variable(VARIABLE_NAME_1)
        expectThat(actualResult)
            .isA<Variable>()
            .isEqualTo(VARIABLE_1)
    }

    @Test
    fun `value has expected result`() {
        val actualResult = value("Mr.Robot")
        expectThat(actualResult)
            .isA<Value<String>>()
            .isEqualTo(VALUE_1)
    }
}