package y2020

import inputLines
import subListsOfSize
import java.lang.IllegalArgumentException

fun main() {
    val expenses = inputLines(2020, 1).map(String::toInt)
    println(part1(expenses))
    println(part2(expenses))
}

private fun part1(expenses: List<Int>): Int {
    val pairs = subListsOfSize(expenses, 2).map { Pair(it[0], it[1]) }
    val matchingPair = pairs.find { it.first + it.second == 2020 } ?: throw IllegalArgumentException("Illegal input")
    return matchingPair.first * matchingPair.second
}

private fun part2(expenses: List<Int>): Int {
    val pairs = subListsOfSize(expenses, 3).map { Triple(it[0], it[1], it[2]) }
    val matchingPair = pairs.find { it.first + it.second + it.third == 2020 } ?: throw IllegalArgumentException("Illegal input")
    return matchingPair.first * matchingPair.second * matchingPair.third
}
