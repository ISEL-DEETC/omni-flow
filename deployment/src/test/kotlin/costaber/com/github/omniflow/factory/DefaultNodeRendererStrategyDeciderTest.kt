package costaber.com.github.omniflow.factory

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import costaber.com.github.omniflow.util.CONDITION_1
import costaber.com.github.omniflow.util.STEP_1
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.Test
import strikt.api.expect
import strikt.assertions.isEqualTo
import kotlin.test.assertFailsWith

internal class DefaultNodeRendererStrategyDeciderTest {

    @Test
    fun `decide renderer has expected results`() {
        val nodeRendStratFactoryMock1 = mockk<NodeRendererStrategyFactory<String>>()
        val nodeRendStratFactoryMock2 = mockk<NodeRendererStrategyFactory<String>>()
        val nodeRendererMock1 = mockk<NodeRenderer<String>>()
        val nodeSlot1 = slot<Node>()

        every {
            nodeRendStratFactoryMock1.getMatcher()
        } returns DefaultPredicate(Step::class)

        every {
            nodeRendStratFactoryMock1.getRenderer(
                capture(nodeSlot1)
            )
        } returns nodeRendererMock1

        every {
            nodeRendStratFactoryMock2.getMatcher()
        } returns DefaultPredicate(Workflow::class)

        val defaultNodeRendererStrategyDecider = DefaultNodeRendererStrategyDecider.Builder()
            .addRendererStrategy(nodeRendStratFactoryMock1)
            .addRendererStrategy(nodeRendStratFactoryMock2)
            .build()
        val actualResult = defaultNodeRendererStrategyDecider.decideRenderer(STEP_1)

        expect {
            that(actualResult).isEqualTo(nodeRendererMock1)
            that(nodeSlot1.captured).isEqualTo(STEP_1)
        }

        verify {
            nodeRendStratFactoryMock1.getMatcher()
            nodeRendStratFactoryMock1.getRenderer(
                nodeSlot1.captured
            )
        }
    }

    @Test
    fun `decideRenderer throws exception when no matcher`() {
        val nodeRendStratFactoryMock1 = mockk<NodeRendererStrategyFactory<String>>()
        val nodeRendStratFactoryMock2 = mockk<NodeRendererStrategyFactory<String>>()

        every {
            nodeRendStratFactoryMock1.getMatcher()
        } returns DefaultPredicate(Step::class)

        every {
            nodeRendStratFactoryMock2.getMatcher()
        } returns DefaultPredicate(Workflow::class)

        val defaultNodeRendererStrategyDecider = DefaultNodeRendererStrategyDecider.Builder()
            .addRendererStrategy(nodeRendStratFactoryMock1)
            .addRendererStrategy(nodeRendStratFactoryMock2)
            .build()
        assertFailsWith<NoSuchElementException> {
            defaultNodeRendererStrategyDecider.decideRenderer(CONDITION_1)
        }

        verify {
            nodeRendStratFactoryMock1.getMatcher()
            nodeRendStratFactoryMock2.getMatcher()
        }
    }
}