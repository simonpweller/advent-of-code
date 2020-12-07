package y2020

import inputLines

fun main() {
    val rules = inputLines(2020, 7)
        .map {
            it.substring(0, it.lastIndex)
        }
        .map { str ->
            val (outer, inner) = str.split(" bags contain ")
            if (inner == "no other bags") Rule(outer, emptyList()) else Rule(outer, inner.split(", ").map {
                Pair(
                    it.substringAfter(" ").substringBeforeLast(" bag"),
                    it.substringBefore(" ").toInt()
                )
            })
        }.associate { it.outer to it.inner }
    println(part1(rules))
    println(part2(rules))
}

private fun part1(rules: Map<String, List<Pair<String, Int>>>): Int {
    val allPossibilities = mutableSetOf("shiny gold")
    var nextPossibilities = setOf("shiny gold")
    while (nextPossibilities.isNotEmpty()) {
        nextPossibilities = nextPossibilities.flatMap { possibility ->
            rules.filter { rule -> rule.value.map { it.first }.contains(possibility) }.map { it.key }
        }.filter { it !in allPossibilities }.toSet()
        allPossibilities.addAll(nextPossibilities)
    }
    return allPossibilities.size - 1
}

private fun part2(rules: Map<String, List<Pair<String, Int>>>): Int {
    return contains("shiny gold", rules)
}

fun contains(bag: String, rules: Map<String, List<Pair<String, Int>>>): Int {
    return rules.getValue(bag).map { (contains(it.first, rules) + 1) * it.second }.sum()
}

data class Rule(val outer: String, val inner: List<Pair<String, Int>>)
