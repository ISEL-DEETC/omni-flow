package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.model.Term
import costaber.com.github.omniflow.model.Variable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class TermResolverTest {

    @ParameterizedTest
    @MethodSource("costaber.com.github.omniflow.util.data.TermDataProvider#termResolveDataProvider")
    fun `resolver has expected results`(term: Term<*>, expectedResult: String) {
        val actualResult = object : TermResolver() {
            // TODO: implement using a term context maybe
            override fun resolveVariable(
                variable: Variable,
                termContext: TermContext
            ): String = variable.name
        }.resolve(term, object : TermContext {})
        expectThat(actualResult).isEqualTo(expectedResult)
    }
}