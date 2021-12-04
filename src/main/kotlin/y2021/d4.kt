package y2021

import inputChunks


fun main() {
    val input = inputChunks(2021, 4)
    val numbers = input.first().split(",").map(String::toInt)
    val boards = input.drop(1)
        .map {
            it
                .split(System.lineSeparator())
                .map { row -> row.split(" ").filter(String::isNotBlank).map(String::toInt) }
        }
        .map(::BingoBoard)

    val scores = getScores(boards, numbers)
    println(scores.first())
    println(scores.last())
}

private fun getScores(boards: List<BingoBoard>, numbers: List<Int>): List<Int> {
    var scores = emptyList<Int>()
    numbers.forEach { number ->
        boards.forEach { board ->
            if (!board.isComplete) {
                board.markNumber(number)
                if (board.isComplete) {
                    scores = scores + board.unmarkedNumbers.sum() * number
                }
            }
        }
    }
    return scores
}

private class BingoBoard(private val rows: List<List<Int>>) {
    var isComplete: Boolean = false
    val numRows = rows.size
    val numCols = rows.first().size
    private val markedNumbers = mutableSetOf<Int>()

    private val numPositions = rows.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, num ->
            num to Pair(rowIndex, colIndex)
        }
    }.toMap()

    val unmarkedNumbers: List<Int>
        get() = rows.flatten().minus(markedNumbers)

    private val markedRows = MutableList(numRows) { 0 }
    private val markedCols = MutableList(numCols) { 0 }

    fun markNumber(number: Int) {
        markedNumbers.add(number)
        val (row, col) = numPositions[number] ?: return
        markedRows[row] = markedRows[row] + 1
        markedCols[col] = markedCols[col] + 1
        if (markedRows[row] == numRows || markedCols[col] == numCols) isComplete = true
    }
}
