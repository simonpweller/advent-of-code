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
    for ((loopIndex, rowIndex) in (0 .. rows.lastIndex step slope.down).withIndex()) {
        val colIndex = loopIndex * slope.right % colLength
        if (rows[rowIndex][colIndex] == '#') count++
    }
    return count
}

private fun part2(rows: List<String>): Long {
    val slopes = listOf(Slope(1, 1), Slope(3, 1), Slope(5, 1), Slope(7, 1), Slope(1, 2))
    return slopes.fold(1L) { multiplied: Long, slope: Slope ->
        multiplied * part1(rows, slope)
    }
}

class Slope(val right: Int, val down: Int)
