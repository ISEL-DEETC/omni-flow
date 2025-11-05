package costaber.com.github.omniflow.visitor

import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.NodeRenderer
import costaber.com.github.omniflow.renderer.RenderingContext
import costaber.com.github.omniflow.util.DUMMY_RENDERING_CONTEXT
import costaber.com.github.omniflow.util.STEP_1
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before
import strikt.api.expect
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class NodeContextVisitorTest {

    @MockK
    private lateinit var nodeRendererStrategyDecider: NodeRendererStrategyDecider

    @InjectMockKs
    private lateinit var nodeContextVisitor: NodeContextVisitor

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun `beginVisit has expected results`() {
        val nodeSlot = slot<Node>()
        val contextSlot = slot<RenderingContext>()

        val nodeRendererMock = mockk<NodeRenderer<*>>()
        val expectedResult = "step {"

        every {
            nodeRendererStrategyDecider.decideRenderer(
                capture(nodeSlot)
            )
        } returns nodeRendererMock

        every {
            nodeRendererMock.beginRender(
                capture(contextSlot)
            )
        } returns expectedResult

        val actualResult = nodeContextVisitor.beginVisit(
            element = STEP_1,
            context = DUMMY_RENDERING_CONTEXT,
        )

        expect {
            that(actualResult).isEqualTo(expectedResult)
            that(nodeSlot.captured).isEqualTo(STEP_1)
            that(contextSlot.captured).isEqualTo(DUMMY_RENDERING_CONTEXT)
        }

        verifyAll {
            nodeRendererStrategyDecider.decideRenderer(
                nodeSlot.captured
            )
            nodeRendererMock.beginRender(
                contextSlot.captured
            )
        }
    }

    @Test
    fun `endVisit has expected results`() {
        val nodeSlot = slot<Node>()
        val contextSlot = slot<RenderingContext>()

        val nodeRendererMock = mockk<NodeRenderer<*>>()
        val expectedResult = "}"

        every {
            nodeRendererStrategyDecider.decideRenderer(
                capture(nodeSlot)
            )
        } returns nodeRendererMock

        every {
            nodeRendererMock.endRender(
                capture(contextSlot)
            )
        } returns expectedResult

        val actualResult = nodeContextVisitor.endVisit(
            element = STEP_1,
            context = DUMMY_RENDERING_CONTEXT,
        )

        expect {
            that(actualResult).isEqualTo(expectedResult)
            that(nodeSlot.captured).isEqualTo(STEP_1)
            that(contextSlot.captured).isEqualTo(DUMMY_RENDERING_CONTEXT)
        }

        verifyAll {
            nodeRendererStrategyDecider.decideRenderer(
                nodeSlot.captured
            )
            nodeRendererMock.endRender(
                contextSlot.captured
            )
        }
    }
}