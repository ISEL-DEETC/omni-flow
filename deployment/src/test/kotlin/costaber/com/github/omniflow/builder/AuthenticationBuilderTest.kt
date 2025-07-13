package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.dsl.authentication
import costaber.com.github.omniflow.model.Authentication
import costaber.com.github.omniflow.util.AUDIENCE_1
import costaber.com.github.omniflow.util.AUTH_TYPE_1
import costaber.com.github.omniflow.util.SCOPES_1
import costaber.com.github.omniflow.util.SCOPE_1
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class AuthenticationBuilderTest {

    @Test
    fun `authentication has expected result`() {
        val actualResult = authentication {
            type(AUTH_TYPE_1)
            scope(SCOPE_1)
            scopes(SCOPES_1)
            audience(AUDIENCE_1)
        }.build()

        expectThat(actualResult)
            .isA<Authentication>()
            .and {
                get { type }.isEqualTo(AUTH_TYPE_1)
                get { scope }.isEqualTo(SCOPE_1)
                get { scopes }.isEqualTo(SCOPES_1)
                get { audience }.isEqualTo(AUDIENCE_1)
            }
    }
}