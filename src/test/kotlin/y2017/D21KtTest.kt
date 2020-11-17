package y2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class D21KtTest {

    @Test
    fun `grids can be iterated on`() {
        val grid = Grid(listOf(
            "../.# => ##./#../...",
            ".#./..#/### => #..#/..../..../#..#",
        ))
        grid.iterate()
        assertEquals("#..#/..../..../#..#", grid.toString())
    }

    @Test
    fun `larger grids apply rules to subGrids`() {
        val grid = Grid(listOf(
            "../.# => ##./#../...",
            ".#./..#/### => #..#/..../..../#..#",
        ))
        grid.iterate()
        grid.iterate()
        assertEquals("##.##./#..#../....../##.##./#..#../......", grid.toString())
    }

    @Test
    fun `get number of lights that are on after 2 iterations`() {
        val grid = Grid(listOf(
            "../.# => ##./#../...",
            ".#./..#/### => #..#/..../..../#..#",
        ))
        grid.iterate()
        grid.iterate()
        assertEquals(12, grid.activePixelCount())
    }

    @Test
    fun `rules can be expanded`() {
        assertEquals(
            mapOf(
                ".#/.." to "##./#../...",
                "#./.." to "##./#../...",
                "../#." to "##./#../...",
                "../.#" to "##./#../...",
                ".#./..#/###" to "#..#/..../..../#..#",
                "#../#.#/##." to "#..#/..../..../#..#",
                "###/#../.#." to "#..#/..../..../#..#",
                ".##/#.#/..#" to "#..#/..../..../#..#",
                ".#./#../###" to "#..#/..../..../#..#",
                "##./#.#/#.." to "#..#/..../..../#..#",
                "###/..#/.#." to "#..#/..../..../#..#",
                "..#/#.#/.##" to "#..#/..../..../#..#",
            ),
            expandRules(listOf(
                "../.# => ##./#../...",
                ".#./..#/### => #..#/..../..../#..#",
            ))
        )
    }

    @Nested
    inner class TwoWidePattern {
        @Test
        fun rotate() {
            val pattern = Pattern("../.#")
            pattern.rotate()
            assertEquals("../#.", pattern.toString())
            pattern.rotate()
            assertEquals("#./..", pattern.toString())
            pattern.rotate()
            assertEquals(".#/..", pattern.toString())
        }

        @Test
        fun flipHorizontally() {
            val pattern = Pattern("../.#")
            pattern.flipHorizontally()
            assertEquals("../#.", pattern.toString())
        }

        @Test
        fun flipVertically() {
            val pattern = Pattern("../.#")
            pattern.flipVertically()
            assertEquals(".#/..", pattern.toString())
        }

        @Test
        fun rotations() {
            val pattern = Pattern("../.#")
            assertEquals(setOf(".#/..", "#./..", "../#.", "../.#"), pattern.rotations())
        }

        @Test
        fun variations() {
            val pattern = Pattern("../.#")
            assertEquals(setOf(".#/..", "#./..", "../#.", "../.#"), pattern.variations())
        }
    }

    @Nested
    inner class ThreeWidePattern {
        @Test
        fun rotate() {
            val pattern = Pattern(".#./..#/###")
            pattern.rotate()
            assertEquals("#../#.#/##.", pattern.toString())
            pattern.rotate()
            assertEquals("###/#../.#.", pattern.toString())
            pattern.rotate()
            assertEquals(".##/#.#/..#", pattern.toString())
        }

        @Test
        fun flipHorizontally() {
            val pattern = Pattern(".#./..#/###")
            pattern.flipHorizontally()
            assertEquals(".#./#../###", pattern.toString())
        }

        @Test
        fun flipVertically() {
            val pattern = Pattern(".#./..#/###")
            pattern.flipVertically()
            assertEquals("###/..#/.#.", pattern.toString())
        }

        @Test
        fun rotations() {
            val pattern = Pattern(".#./..#/###")
            assertEquals(setOf(".#./..#/###", "#../#.#/##.", "###/#../.#.", ".##/#.#/..#"), pattern.rotations())
        }

        @Test
        fun variations() {
            val pattern = Pattern(".#./..#/###")
            assertEquals(
                setOf(
                    ".#./..#/###", "#../#.#/##.", "###/#../.#.", ".##/#.#/..#",
                    ".#./#../###", "##./#.#/#..", "###/..#/.#.", "..#/#.#/.##",
                ),
                pattern.variations()
            )
        }
    }
}
