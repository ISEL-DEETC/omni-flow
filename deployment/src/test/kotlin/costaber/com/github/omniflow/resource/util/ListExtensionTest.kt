package costaber.com.github.omniflow.resource.util

import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class ListExtensionTest {

    @Test
    fun `joinToStringNewLines has expected results`() {
        val list = listOf(1, 2, 3, 4, 5)
        val expectedResult = "1\n2\n3\n4\n5"

        val actualResult = list.joinToStringNewLines()

        expectThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `joinToStringNewLines for empty list`() {
        val actualResult = emptyList<String>().joinToStringNewLines()
        expectThat(actualResult).isEqualTo("")
    }
}