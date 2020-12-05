package y2020

import inputLines

fun main() {
    val boardingPasses = inputLines(2020, 5).map(::BoardingPass)
    println(boardingPasses.map { it.seatId }.maxOrNull())
    println(part2(boardingPasses))
}

fun part2(boardingPasses: List<BoardingPass>): Int {
    val allSeats = (0..127).flatMap { row ->
        (0..7).map { col ->
            BoardingPass(row, col)
        }
    }
    val yourSeat = allSeats.first {
        it !in boardingPasses && BoardingPass(it.row - 1, it.col) in boardingPasses && BoardingPass(
            it.row + 1,
            it.col
        ) in boardingPasses
    }
    return yourSeat.seatId
}

data class BoardingPass(val row: Int, val col: Int) {
    constructor(boardingPassNumber: String) :
            this(getRow(boardingPassNumber), getCol(boardingPassNumber))

    val seatId: Int = row * 8 + col
}

fun getRow(boardingPassNumber: String) =
    boardingPassNumber.substring(0, 7).fold(0..127) { range, char ->
        when (char) {
            'F' -> range.lowerHalf
            'B' -> range.upperHalf
            else -> throw NotImplementedError()
        }
    }.first

fun getCol(boardingPassNumber: String) =
    boardingPassNumber.substring(7, 10).fold(0..7) { range, char ->
        when (char) {
            'L' -> range.lowerHalf
            'R' -> range.upperHalf
            else -> throw NotImplementedError()
        }
    }.first


val IntRange.size: Int
    get() = last - first + 1

val IntRange.lowerHalf: IntRange
    get() = first..last - size / 2

val IntRange.upperHalf: IntRange
    get() = first + size / 2..last
