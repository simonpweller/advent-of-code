package y2020

import inputLines

fun main() {
    val rows = inputLines(2020, 3)
    println(part1(rows))
    println(part2(rows))
}

private fun part1(rows: List<String>, slope: Slope = Slope(3, 1)): Int {
    val colLength = rows.first().length
    var count = 0
    var colIndex = 0
    (0..rows.lastIndex step slope.down).forEach { rowIndex ->
        if (rows[rowIndex][colIndex] == '#') count++
        colIndex = (colIndex + slope.right) % colLength
    }
    return count
}

private fun part2(rows: List<String>): Long =
    listOf(Slope(1, 1), Slope(3, 1), Slope(5, 1), Slope(7, 1), Slope(1, 2))
        .map { slope -> part1(rows, slope) }
        .fold(1) { total, treesHit -> total * treesHit }

class Slope(val right: Int, val down: Int)
