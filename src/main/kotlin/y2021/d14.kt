package y2021

import inputLines
import java.util.Collections.max
import java.util.Collections.min

fun main() {
    val input = inputLines(2021, 14)
    val polymerTemplate = input.first()
    val insertionRules = input.drop(2).associate { it.split(" -> ").let { (pair, insertion) -> pair to insertion } }

    println(part1(polymerTemplate, insertionRules))
}

private fun part1(polymerTemplate: String, insertionRules: Map<String, String>): Int =
    generateSequence(polymerTemplate) { polymer -> insert(polymer, insertionRules) }
        .drop(1).take(10).last()
        .let { finalPolymer ->
            finalPolymer.groupingBy { it }.eachCount().let { counts ->
                max(counts.values) - min(counts.values)
            }
        }

private fun insert(polymerTemplate: String, insertionRules: Map<String, String>): String =
    polymerTemplate.windowed(2).map { pair -> insertionRules[pair] }.joinToString("").let { inserts ->
        polymerTemplate.zip(inserts).flatMap { listOf(it.first, it.second) }.plus(polymerTemplate.last())
            .joinToString("")
    }
