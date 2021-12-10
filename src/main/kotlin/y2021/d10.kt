package y2021

import inputLines

fun main() {
    val lines = inputLines(2021, 10).map(::ProgramLine)
    println(part1(lines))
    println(part2(lines))
}

private fun part1(lines: List<ProgramLine>) = lines
    .sumOf { it.syntaxErrorScore }

private fun part2(lines: List<ProgramLine>) = lines
    .filterNot { it.isCorrupted }.let { incompleteLines ->
        incompleteLines.map { it.completionStringScore }.sorted()[incompleteLines.size / 2]
    }

private data class ProgramLine(val brackets: String) {
    val unmatchedBrackets: List<Bracket>
        get() = brackets
            .fold(emptyList()) { unmatchedBrackets, bracket ->
                when {
                    bracket.isClosing && bracket.match == unmatchedBrackets.last() -> unmatchedBrackets.dropLast(1)
                    else -> unmatchedBrackets.plus(bracket)
                }
            }

    val isCorrupted: Boolean
        get() = unmatchedBrackets.any { it.isClosing }

    val syntaxErrorScore: Int
        get() = unmatchedBrackets
            .firstOrNull { it.isClosing }?.illegalCharacterScore ?: 0

//  New!
    val completionStringScore: Long
        get() = unmatchedBrackets
            .reversed()
            .fold(0L) { score, bracket -> score * 5 + bracket.closingCharacterScores }
}

typealias Bracket = Char

private val Bracket.isClosing: Boolean
    get() = setOf(')', ']', '}', '>').contains(this)

private val Bracket.illegalCharacterScore: Int
    get() = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException("Not a closing bracket")
    }

private val Bracket.closingCharacterScores: Int
    get() = when (this) {
        '(' -> 1
        '[' -> 2
        '{' -> 3
        '<' -> 4
        else -> throw IllegalArgumentException("Not an opening bracket")
    }

private val Bracket.match: Char
    get() = when (this) {
        ')' -> '('
        '(' -> ')'
        ']' -> '['
        '[' -> ']'
        '}' -> '{'
        '{' -> '}'
        '>' -> '<'
        '<' -> '>'
        else -> throw IllegalArgumentException("Unexpected character")
    }
