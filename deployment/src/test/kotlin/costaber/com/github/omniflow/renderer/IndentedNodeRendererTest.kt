package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.model.Node
import io.mockk.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class IndentedNodeRendererTest {

    private companion object {
        const val RENDER_RESULT = "{ something }"
    }

    @Test
    fun `beginRender has expected results`() {
        val indentedRenderingContext = mockk<IndentedRenderingContext>()
        val caller = mockk<() -> Unit>()
        val indentedNodeRenderer = IndentedNodeRendererForTest(caller)

        every {
            indentedRenderingContext.incIndentationLevel()
        } just Runs

        every {
            caller()
        } just Runs

        val actualResult = indentedNodeRenderer.beginRender(indentedRenderingContext)

        expectThat(actualResult).isEqualTo(RENDER_RESULT)

        verifyOrder {
            caller()
            indentedRenderingContext.incIndentationLevel()
        }
    }

    @Test
    fun `endRender has expected results`() {
        val indentedRenderingContext = mockk<IndentedRenderingContext>()
        val caller = mockk<() -> Unit>()
        val indentedNodeRenderer = IndentedNodeRendererForTest(caller)

        every {
            indentedRenderingContext.decIndentationLevel()
        } just Runs

        every {
            caller()
        } just Runs

        val actualResult = indentedNodeRenderer.endRender(indentedRenderingContext)

        expectThat(actualResult).isEqualTo(RENDER_RESULT)

        verifyOrder {
            indentedRenderingContext.decIndentationLevel()
            caller()
        }
    }

    class IndentedNodeRendererForTest(val caller: () -> Unit) : IndentedNodeRenderer() {
        override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
            caller()
            return RENDER_RESULT
        }

        override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
            caller()
            return RENDER_RESULT
        }

        override val element: Node = object : Node {
            override fun childNodes() = emptyList<Node>()
        }
    }
}