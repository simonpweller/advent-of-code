package y2020

import charCounts
import inputLines

fun main() {
    println(inputLines(2020, 2)
        .map(::parseInputLine)
        .filter { it.first.fulfilledBy(it.second) }
        .count()
    )

    println(inputLines(2020, 2)
        .map(::parseInputLine)
        .filter { it.first.officiallyFulfilledBy(it.second) }
        .count()
    )
}

fun parseInputLine(line: String): Pair<PasswordRule, String> {
    val (rule, password) = line.split(": ")
    return Pair(PasswordRule.of(rule), password)
}

data class PasswordRule(val min: Int, val max: Int, val char: Char) {
    fun fulfilledBy(password: String): Boolean {
        val charCount = charCounts(password)[char] ?: 0
        return charCount in min..max
    }

    fun officiallyFulfilledBy(password: String): Boolean {
        return (password[min - 1] == char) xor (password[max - 1] == char)
    }

    companion object {
        fun of(string: String): PasswordRule {
            val (minMax, charString) = string.split(" ")
            val (minString, maxString) = minMax.split("-")
            return PasswordRule(minString.toInt(), maxString.toInt(), charString.first())
        }
    }
}
