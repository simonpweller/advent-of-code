package y2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class D24KtTest {

    @Test
    fun `input can be parsed to list (set?) of Bricks`() {
        val input = listOf(
            "0/2",
            "2/2",
            "2/3",
            "3/4",
            "3/5",
            "0/1",
            "10/1",
            "9/10",
        )
        assertEquals(
            setOf(
                Brick(0, 2),
                Brick(2, 2),
                Brick(2, 3),
                Brick(3, 4),
                Brick(3, 5),
                Brick(0, 1),
                Brick(10, 1),
                Brick(9, 10),
            ), parseInput(input)
        )
    }

    @Test
    fun `validBridges returns a set of valid bridges for a set of Bricks`() {
        assertEquals(
            setOf(
                Bridge(),
                Bridge(listOf(Brick(0, 1))),
                Bridge(listOf(Brick(0, 1), Brick(10, 1))),
                Bridge(listOf(Brick(0, 1), Brick(10, 1), Brick(9, 10))),
                Bridge(listOf(Brick(0, 2))),
                Bridge(listOf(Brick(0, 2), Brick(2, 3))),
                Bridge(listOf(Brick(0, 2), Brick(2, 3), Brick(3, 4))),
                Bridge(listOf(Brick(0, 2), Brick(2, 3), Brick(3, 5))),
                Bridge(listOf(Brick(0, 2), Brick(2, 2))),
                Bridge(listOf(Brick(0, 2), Brick(2, 2), Brick(2, 3))),
                Bridge(listOf(Brick(0, 2), Brick(2, 2), Brick(2, 3), Brick(3, 4))),
                Bridge(listOf(Brick(0, 2), Brick(2, 2), Brick(2, 3), Brick(3, 5))),
            ), validBridges(
                setOf(
                    Brick(0, 2),
                    Brick(2, 2),
                    Brick(2, 3),
                    Brick(3, 4),
                    Brick(3, 5),
                    Brick(0, 1),
                    Brick(10, 1),
                    Brick(9, 10),
                )
            )
        )
    }
}
