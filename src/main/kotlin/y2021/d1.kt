package y2021

import inputLines

fun main() {
    val measurements = inputLines(2021, 1).map(String::toLong)
    println(part1(measurements))
    println(part2(measurements))
}

fun part1(measurements: List<Long>): Int = measurements
    .windowed(2)
    .count { it.last() > it.first() }

fun part2(measurements: List<Long>): Int = measurements
    .windowed(3)
    .windowed(2)
    .count { it.last().sum() > it.first().sum() }
