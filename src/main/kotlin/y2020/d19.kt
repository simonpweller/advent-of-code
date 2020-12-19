package y2020

import inputChunks

fun main() {
    val inputChunks = inputChunks(2020, 19)
    val ruleStrings = inputChunks.first().lines().map { it.substringBefore(":").toInt() to it.substringAfter(": ") }.toMap()
    val ruleset = Ruleset(ruleStrings)
    val rules = ruleset.get(0).toSet()
    val messages = inputChunks[1].lines()

    println(messages.filter { it in rules }.size)
    println(part2(ruleset, messages))
}

private fun part2(ruleset: Ruleset, messages: List<String>): Int {
    // a valid message for part 2 will be a combination of n chunks matching 42 + m chunks matching 31, where
    // 1: n is >= 2
    // 2: m is >= 1
    // 3: n is > m

    val match42 = ruleset.get(42).toSet()
    val match31 = ruleset.get(31).toSet()

    return messages.filter { message ->
        val chunks = message.chunked(8)
        // first two chunks need to be 42-based, last one needs to be 31-based (non-nested 8 + non-nested 11)
        if (!match42.contains(chunks[0]) || !match42.contains(chunks[1]) || !match31.contains(chunks.last())) return@filter false
        // must be at least one more match for 42 than for 31 (nesting 11s adds a 42 for each 11 + at least one 42 for the 8)
        val firstChunkNotMatching42 = chunks.indexOfFirst { chunk -> !match42.contains(chunk) }
        // all chunks after the last chunk that matches 42 have to match 31
        if (firstChunkNotMatching42 <= (chunks.size - firstChunkNotMatching42)) return@filter false
        chunks.subList(firstChunkNotMatching42, chunks.size).all { match31.contains(it) }
    }.size
}

private class Ruleset(val rules: Map<Int, String>) {
    val cache = mutableMapOf<Int, List<String>>()

    fun get(key: Int): List<String> {
        if (cache.containsKey(key)) return cache.getValue(key)
        val value = rules.getValue(key)
        val rules = when {
            value.contains('"') -> listOf(value.substringAfter('"').substringBefore('"'))
            value.contains('|') -> value.split(" | ").map { it.split(" ").map(String::toInt) }
                .map { subRule -> subRule.fold(listOf("")) {listA, curr -> combine(listA, get(curr))} }
                .flatten()
            else -> value.split(" ").map(String::toInt).fold(listOf("")) { listA, curr ->
                combine(listA, get(curr))
            }
        }
        cache[key] = rules
        return rules
    }

    fun combine(listA: List<String>, listB: List<String>): List<String> = listA.flatMap { ruleA -> listB.map { ruleB -> ruleA + ruleB } }
}
