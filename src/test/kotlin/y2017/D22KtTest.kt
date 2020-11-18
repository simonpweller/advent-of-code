package y2017

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class D22KtTest {

    @Test
    fun `input can be parsed to a set of infected positions`() {
        assertEquals(mutableSetOf(Point(1, -1), Point(-1, 0)), infectedNodes(listOf("..#", "#..", "...")))
    }

    @Test
    fun `the VirusCarrier begins at the origin facing up and with an infectedCount of 0`() {
        val virusCarrier = VirusCarrier(mutableSetOf())
        assertEquals(Point(0, 0), virusCarrier.position)
        assertEquals(Direction.UP, virusCarrier.direction)
        assertEquals(0, virusCarrier.infectedCount)
    }

    @Nested
    inner class PositionInfected {

        @Test
        fun `virus carrier turns right`() {
            val virusCarrier = VirusCarrier(mutableSetOf(Point(0, 0)))
            virusCarrier.burst()
            assertEquals(Direction.RIGHT, virusCarrier.direction)
        }

        @Test
        fun `current node becomes clean`() {
            val virusCarrier = VirusCarrier(mutableSetOf(Point(0, 0)))
            virusCarrier.burst()
            assertFalse(virusCarrier.infectedPoints.contains(Point(0, 0)))
            assertEquals(0, virusCarrier.infectedCount)
        }

        @Test
        fun `virus carrier moves forward`() {
            val virusCarrier = VirusCarrier(mutableSetOf(Point(0, 0)))
            virusCarrier.burst()
            assertEquals(Point(1, 0), virusCarrier.position)
        }
    }


    @Nested
    inner class PositionNotInfected {

        @Test
        fun `virus carrier turns left`() {
            val virusCarrier = VirusCarrier(mutableSetOf())
            virusCarrier.burst()
            assertEquals(Direction.LEFT, virusCarrier.direction)
        }

        @Test
        fun `current node becomes infected`() {
            val virusCarrier = VirusCarrier(mutableSetOf())
            virusCarrier.burst()
            assertTrue(virusCarrier.infectedPoints.contains(Point(0, 0)))
            assertEquals(1, virusCarrier.infectedCount)
        }

        @Test
        fun `virus carrier moves forward`() {
            val virusCarrier = VirusCarrier(mutableSetOf())
            virusCarrier.burst()
            assertEquals(Point(-1, 0), virusCarrier.position)
        }
    }

    @Test
    fun `infectedCount is 5 after 7 bursts`() {
        val virusCarrier = VirusCarrier(mutableSetOf(Point(1, -1), Point(-1, 0)))
        virusCarrier.burst(7)
        assertEquals(5, virusCarrier.infectedCount)
    }

    @Test
    fun `infectedCount is 41 after 70 bursts`() {
        val virusCarrier = VirusCarrier(mutableSetOf(Point(1, -1), Point(-1, 0)))
        virusCarrier.burst(70)
        assertEquals(41, virusCarrier.infectedCount)
    }

    @Test
    fun `infectedCount is 5587 after 10000 bursts`() {
        val virusCarrier = VirusCarrier(mutableSetOf(Point(1, -1), Point(-1, 0)))
        virusCarrier.burst(10000)
        assertEquals(5587, virusCarrier.infectedCount)
    }

    @Test
    fun `infectedCount is 26 after 100 part2 bursts`() {
        val virusCarrier = VirusCarrier(mutableSetOf(Point(1, -1), Point(-1, 0)))
        virusCarrier.burstPart2(100)
        assertEquals(26, virusCarrier.infectedCount)
    }

    @Test
    fun `infectedCount is 2511944 after 10000000 part2 bursts`() {
        val virusCarrier = VirusCarrier(mutableSetOf(Point(1, -1), Point(-1, 0)))
        virusCarrier.burstPart2(10000000)
        assertEquals(2511944, virusCarrier.infectedCount)
    }
}
