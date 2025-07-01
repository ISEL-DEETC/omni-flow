package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.util.CALL_CONTEXT_1
import costaber.com.github.omniflow.util.STEP_1
import costaber.com.github.omniflow.visitor.ContextVisitor
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Before
import strikt.api.expect
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class DepthFirstNodeVisitorTraversorTest {

    @InjectMockKs
    private lateinit var depthFirstNodeVisitorTraversor: DepthFirstNodeVisitorTraversor

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun `traverse has expected results`() {
        val beginNodeSlots = mutableListOf<Node>()
        val beginContextSlots = mutableListOf<Int>()
        val endNodeSlots = mutableListOf<Node>()
        val endContextSlots = mutableListOf<Int>()

        val visitor = mockk<ContextVisitor<Node, Int, String>>()
        var idx = 0

        every {
            visitor.beginVisit(
                capture(beginNodeSlots),
                capture(beginContextSlots),
            )
        } answers {
            val idxStr = idx.toString()
            idx++
            idxStr
        }

        every {
            visitor.endVisit(
                capture(endNodeSlots),
                capture(endContextSlots),
            )
        } answers {
            val idxStr = idx.toString()
            idx--
            idxStr
        }

        val expectedResult = listOf("0", "1", "2", "1")

        val actualResult = depthFirstNodeVisitorTraversor.traverse(
            visitor = visitor,
            root = STEP_1,
            context = 0,
        )

        expect {
            that(actualResult).isEqualTo(expectedResult)
            that(beginNodeSlots).containsExactly(STEP_1, CALL_CONTEXT_1)
            that(beginContextSlots).containsExactly(0, 0)
            that(endNodeSlots).containsExactly(CALL_CONTEXT_1, STEP_1)
            that(endContextSlots).containsExactly(0, 0)
            that(idx).isEqualTo(0)
        }

        verify {
            for (i in 0 until beginNodeSlots.size) {
                visitor.beginVisit(
                    beginNodeSlots[i],
                    beginContextSlots[i],
                )
                visitor.endVisit(
                    endNodeSlots[i],
                    endContextSlots[i],
                )
            }
        }

        confirmVerified(visitor)
    }
}