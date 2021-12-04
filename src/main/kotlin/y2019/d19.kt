package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 19).split(",").map(String::toLong)

    println(part1(intcode))
    println(part2(intcode))
}

private fun part1(intcode: List<Long>): Long {
    return (0L until 50).flatMap { x ->
        (0L until 50).map { y ->
            IntcodeComputer(intcode, listOf(x, y)).run().first()
        }
    }.sum()
}

private fun part2(intcode: List<Long>): Long {
    val map = mutableMapOf<Long, Pair<Long, Long>>()

    var x = 0L
    while (IntcodeComputer(intcode, listOf(x, 90L)).run().first() == 0L) { x++ }
    var startX = x
    while (IntcodeComputer(intcode, listOf(x, 90L)).run().first() == 1L) { x++ }
    var endX = x - 1

    (91L .. 10000).forEach { y ->
        while (IntcodeComputer(intcode, listOf(startX, y)).run().first() == 0L) { startX++ }
        while (IntcodeComputer(intcode, listOf(endX, y)).run().first() == 1L) { endX++ }
        endX--
        map[y] = Pair(startX, endX)
    }

    val r = (191L .. 10000).find {
        map.getValue(it).first + 100 <= map.getValue(it - 100).second
    } ?: throw IllegalArgumentException("No solution")

    val sy = r - 99
    val sx = map.getValue(r).first

    return sx * 10_000 + sy
}
