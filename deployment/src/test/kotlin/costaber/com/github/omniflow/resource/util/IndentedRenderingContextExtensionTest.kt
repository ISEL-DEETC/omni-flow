package costaber.com.github.omniflow.resource.util

import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.TermContext
import kotlin.test.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class IndentedRenderingContextExtensionTest {

    @Test
    fun `render has expected results`() {
        val indentedRenderingContext = IndentedRenderingContext(
            indentationLevel = 0,
            stringBuilder = StringBuilder(),
            termContext = object : TermContext {},
        )
        val expectedResult = "Example 1!\nExample 2!\n"

        val actualResult = render(indentedRenderingContext) {
            add("Example 1")
            append("!")
            addEmptyLine()
            addLine("Example 2!")
        }

        expectThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `render returns empty string`() {
        val indentedRenderingContext = IndentedRenderingContext(
            indentationLevel = 0,
            stringBuilder = StringBuilder(),
            termContext = object : TermContext {},
        )
        val expectedResult = ""

        val actualResult = render(indentedRenderingContext) {}

        expectThat(actualResult).isEqualTo(expectedResult)
    }
}