package y2017

import inputLines
import java.lang.IllegalStateException

fun main() {
    val grid = Grid(inputLines(2017, 21))
    grid.iterate(5)
    println(grid.activePixelCount())

    grid.iterate(13)
    println(grid.activePixelCount())
}

private class Grid(rules: List<String>) {
    private var rows = stringToRows(".#./..#/###")
    private val expandedRules = expandRules(rules)

    fun iterate(times: Int) {
        repeat(times) { this.iterate() }
    }

    fun iterate() {
        rows = if (rows.size % 2 == 0) {
            val rowChunks = rows.chunked(2)
            rowChunks.map { rowChunk ->
                val secondRowChunks = rowChunk[1].chunked(2)
                val chunks = rowChunk[0].chunked(2).mapIndexed { index, c -> listOf(c, secondRowChunks[index]) }
                val transformedChunks = chunks.map { expandedRules[rowsToString(it)] ?: throw IllegalStateException("No matching rule!") }.map(::stringToRows)
                listOf(transformedChunks.map { it.first() }.flatten(), transformedChunks.map { it[1] }.flatten(), transformedChunks.map { it[2] }.flatten())
            }.flatten()
        } else {
            val rowChunks = rows.chunked(3)
            rowChunks.map { rowChunk ->
                val secondRowChunks = rowChunk[1].chunked(3)
                val thirdRowChunks = rowChunk[2].chunked(3)
                val chunks = rowChunk[0].chunked(3).mapIndexed { index, c -> listOf(c, secondRowChunks[index], thirdRowChunks[index]) }
                val transformedChunks = chunks.map { expandedRules[rowsToString(it)] ?: throw IllegalStateException("No matching rule!") }.map(::stringToRows)
                listOf(transformedChunks.map { it.first() }.flatten(), transformedChunks.map { it[1] }.flatten(), transformedChunks.map { it[2] }.flatten(), transformedChunks.map { it[3] }.flatten())
            }.flatten()
        }
    }

    fun activePixelCount(): Int = rows.flatten().count {it == '#'}

    override fun toString() = rowsToString(rows)
}

private fun expandRules(rules: List<String>): Map<String, String> =
    rules.flatMap { rule ->
        val (before, after) = rule.split(" => ")
        Pattern(before).variations().map { it to after }
    }.toMap()

private fun stringToRows(string: String) = string.split('/').map(String::toList)
private fun rowsToString(rows: List<List<Char>>) = rows.joinToString("/") { it.joinToString("") }

private class Pattern(string: String) {
    private var rows = stringToRows(string)

    fun rotate() {
        val rotatedRows = Array(rows[0].size) { CharArray(rows.size) }
        for (i in rows.indices) {
            for (j in rows[0].indices) {
                rotatedRows[j][rows.lastIndex - i] = rows[i][j]
            }
        }
        rows = rotatedRows.map(CharArray::toList)
    }

    fun flipHorizontally() {
        rows = rows.map { it.reversed() }
    }

    fun flipVertically() {
        rows = rows.reversed()
    }

    override fun toString() = rowsToString(rows)

    fun rotations(): Set<String> {
        val rotations = mutableSetOf<String>()
        rotations.add(toString())
        rotate()
        rotations.add(toString())
        rotate()
        rotations.add(toString())
        rotate()
        rotations.add(toString())
        rotate()
        return rotations
    }

    fun variations(): Set<String> {
        val variations = mutableSetOf<String>()
        variations.addAll(rotations())
        flipHorizontally()
        variations.addAll(rotations())
        flipHorizontally()
        flipVertically()
        variations.addAll(rotations())
        flipVertically()
        return variations
    }
}
