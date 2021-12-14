package y2021

import inputLines
import java.util.Collections.max
import java.util.Collections.min

fun main() {
    val input = inputLines(2021, 14)
    val polymerTemplate = input.first()
    val insertionRules = input.drop(2).associate {
        it.split(" -> ").let { (pair, insertion) ->
            pair to Pair(pair.first() + insertion, insertion + pair.last())
        }
    }

    println(solve(polymerTemplate, insertionRules, 10))
    println(solve(polymerTemplate, insertionRules, 40))
}

private fun solve(polymerTemplate: String, insertionRules: Map<String, Pair<String, String>>, iterations: Int): Long {
    val pairCounts =
        polymerTemplate.windowed(2).groupingBy { it }.eachCount().mapValues { (_, value) -> value.toLong() }
    val finalPairCounts = generateSequence(pairCounts) { insert(it, insertionRules) }.drop(iterations).take(1).first()
    val letterCounts = getLetterCounts(finalPairCounts, polymerTemplate)

    return max(letterCounts.values) - min(letterCounts.values)
}

private fun getLetterCounts(
    pairCounts: Map<String, Long>,
    polymerTemplate: String
): Map<Char, Long> {
    val letterCounts = mutableMapOf<Char, Long>()
    pairCounts.entries.forEach { (key, value) ->
        letterCounts[key.first()] = letterCounts.getOrDefault(key.first(), 0) + value
        letterCounts[key.last()] = letterCounts.getOrDefault(key.last(), 0) + value
    }
    // all but first & last char are in two pairs, so have to deduplicate counts
    return letterCounts.entries.associate { (key, value) ->
        when (key) {
            polymerTemplate.first(), polymerTemplate.last() -> key to (value + 1) / 2
            else -> key to value / 2
        }
    }
}

private fun insert(
    polymerTemplate: Map<String, Long>,
    insertionRules: Map<String, Pair<String, String>>
): Map<String, Long> = mutableMapOf<String, Long>().also { next ->
    polymerTemplate.forEach { (pair, count) ->
        val (leftNew, rightNew) = insertionRules.getValue(pair)
        next[leftNew] = next.getOrDefault(leftNew, 0) + count
        next[rightNew] = next.getOrDefault(rightNew, 0) + count
    }
}
