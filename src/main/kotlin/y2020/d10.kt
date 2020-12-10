package y2020

import inputLines

fun main() {
    val input = inputLines(2020, 10).map(String::toInt)

    val adapters = input
        .asSequence()
        .plus(0)
        .plus(input.maxOrNull()!! + 3)

    val part1 = adapters
        .sorted()
        .zipWithNext().map { it.second - it.first }
        .toList()
        .let { diffs -> diffs.count { it == 1 } * diffs.count { it == 3 } }

    val part2 = adapters
        .sortedDescending()
        .drop(1)
        .fold(mapOf(adapters.maxOrNull()!! to 1L)) { map, num ->
            val ways = (1 .. 3).map { map.getOrDefault(num + it, 0) }.sum()
            map.plus(num to ways)
        }
        .getValue(0)

    println(part1)
    println(part2)
}


