package y2018

import inputLines
import kotlin.math.abs

fun main() {
    val points = inputLines(2018, 25).map { line -> line.split(",").map { it.toInt() }.let { FourDPoint(it[0], it[1], it[2], it[3]) } }
    var groups = listOf<List<FourDPoint>>()

    points.forEach { point ->
        val groupsInRange = groups.filter { group -> group.any { it.inRangeOf(point) } }
        val groupsOutOfRange = groups.filter { group -> group.none { it.inRangeOf(point) } }

        val mergedGroup = groupsInRange.fold(listOf(point)) { mergedGroup, group -> mergedGroup.plus(group)}
        groups = groupsOutOfRange.plus(listOf(mergedGroup))
    }

    println(groups.size)
}

private data class FourDPoint(val d1: Int, val d2: Int, val d3: Int, val d4: Int) {
    fun inRangeOf(other: FourDPoint): Boolean = manhattanDistanceTo(other) <= 3

    private fun manhattanDistanceTo(other: FourDPoint): Int =
        abs(other.d1 - d1) + abs(other.d2 - d2) + abs(other.d3 - d3) + abs(other.d4 - d4)
}
