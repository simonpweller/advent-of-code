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
    .filterNot { it.hasSyntaxError }.let { incompleteLines ->
        incompleteLines.map { it.completionStringScore }.sorted()[incompleteLines.size / 2]
    }

private data class ProgramLine(val chars: String) {
    val unmatchedBrackets: List<Char>
        get() = chars
            .fold(emptyList()) { unmatchedBrackets, char ->
                when {
                    char.isOpeningBracket -> unmatchedBrackets.plus(char)
                    char.matchingBracket != unmatchedBrackets.last() -> unmatchedBrackets.plus(char)
                    else -> unmatchedBrackets.dropLast(1)
                }
            }

    val hasSyntaxError: Boolean
        get() = unmatchedBrackets.any { it.isClosingBracket }

    val syntaxErrorScore: Int
        get() = unmatchedBrackets
            .firstOrNull { it.isClosingBracket }?.illegalCharacterScore ?: 0

    val completionStringScore: Long
        get() = unmatchedBrackets
            .reversed()
            .fold(0L) { score, char -> score * 5 + char.closingCharacterScores }
}

private val Char.isOpeningBracket: Boolean
    get() = setOf('(', '[', '{', '<').contains(this)

private val Char.isClosingBracket: Boolean
    get() = setOf(')', ']', '}', '>').contains(this)

private val Char.illegalCharacterScore: Int
    get() = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException("Not a closing bracket")
    }

private val Char.closingCharacterScores: Int
    get() = when (this) {
        '(' -> 1
        '[' -> 2
        '{' -> 3
        '<' -> 4
        else -> throw IllegalArgumentException("Not an opening bracket")
    }

private val Char.matchingBracket: Char
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
