package y2021

import inputLines

fun main() {
    val input = inputLines(2021, 11)
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val octopusCavern = OctopusCavern.of(input)
    var totalFlashes = 0
    repeat(100) {
        totalFlashes += octopusCavern.step()
    }
    return totalFlashes
}

private fun part2(input: List<String>): Int {
    val octopusCavern = OctopusCavern.of(input)
    val totalSize = octopusCavern.rows.sumOf { it.size }
    var steps = 0
    do {
        val flashCount = octopusCavern.step()
        steps++
    } while (flashCount != totalSize)
    return steps
}

private class OctopusCavern(var rows: List<List<Int>>) {
    val coordinates =
        rows.indices.flatMap { rowIndex -> rows.first().indices.map { colIndex -> Pair(rowIndex, colIndex) } }

    fun step(): Int {
        val flashed = this.rows.map { row -> row.map { false }.toMutableList() }
        val next = this.rows.map { row -> row.map { it + 1 }.toMutableList() }
        do {
            var anyNewFlashes = false
            coordinates.forEach { (row, col) ->
                if (next[row][col] > 9 && !flashed[row][col]) {
                    flashed[row][col] = true
                    anyNewFlashes = true
                    getNeighbours(row, col).forEach { (row, col) -> next[row][col] = next[row][col] + 1 }
                }
            }
        } while (anyNewFlashes)
        coordinates.forEach { (row, col) -> if (flashed[row][col]) next[row][col] = 0 }
        this.rows = next
        return coordinates.count { (row, col) -> flashed[row][col] }
    }

    override fun toString() = rows.joinToString(System.lineSeparator()) { it.joinToString("") }

    fun getNeighbours(rowIndex: Int, colIndex: Int) =
        listOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, -1), Pair(0, 1), Pair(1, -1), Pair(1, 0), Pair(1, 1))
            .map { Pair(rowIndex + it.first, colIndex + it.second) }
            .filter { (row, col) -> row >= 0 && row <= rows.lastIndex && col >= 0 && col <= rows.first().lastIndex }

    companion object {
        fun of(input: List<String>): OctopusCavern =
            OctopusCavern(input.map { it.map(Char::toString).map(String::toInt) })
    }
}
