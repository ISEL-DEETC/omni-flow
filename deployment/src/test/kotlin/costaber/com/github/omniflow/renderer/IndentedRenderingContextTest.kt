package costaber.com.github.omniflow.renderer

import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class IndentedRenderingContextTest {

    @Test
    fun `getString has expected results`() {
        // TODO : improve maybe
        val indentedRenderingContext = IndentedRenderingContext(termContext = object : TermContext {})

        val expectedResult = "Line1!\nLine2!\nLine3!\n    Line4!\n    Line5!"

        indentedRenderingContext.addLine("Line1!")
        indentedRenderingContext.addLine("Line2!")
        indentedRenderingContext.add("Line3")
        indentedRenderingContext.append("!")
        indentedRenderingContext.addEmptyLine()
        indentedRenderingContext.tab {
            addLine("Line4!")
        }
        indentedRenderingContext.incIndentationLevel()
        indentedRenderingContext.add("Line5!")
        indentedRenderingContext.decIndentationLevel()

        val actualResult = indentedRenderingContext.getString()

        expectThat(actualResult).isEqualTo(expectedResult)
    }
}