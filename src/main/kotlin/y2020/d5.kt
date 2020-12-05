package y2020

import inputLines

fun main() {
    val seats = inputLines(2020, 5).map(BoardingPassScanner::scan)
    println(seats.map(Seat::seatId).maxOrNull())
    println(part2(seats))
}

private fun part2(takenSeats: List<Seat>): Int {
    val allSeats = (0..127).flatMap { row ->
        (0..7).map { col ->
            Seat(row, col)
        }
    }
    val yourSeat = allSeats.first {
        it !in takenSeats
                && Seat(it.row - 1, it.col) in takenSeats
                && Seat(it.row + 1, it.col) in takenSeats
    }
    return yourSeat.seatId
}

private data class Seat(val row: Int, val col: Int) {
    val seatId: Int = row * 8 + col
}


private class BoardingPassScanner {
    companion object {
        fun scan(boardingPassNumber: String): Seat =
            Seat(scanRow(boardingPassNumber), scanCol(boardingPassNumber))

        private fun scanRow(boardingPassNumber: String) =
            boardingPassNumber.substring(0, 7).fold(0..127) { range, char ->
                when (char) {
                    'F' -> range.lowerHalf
                    'B' -> range.upperHalf
                    else -> throw NotImplementedError()
                }
            }.first

        private fun scanCol(boardingPassNumber: String) =
            boardingPassNumber.substring(7, 10).fold(0..7) { range, char ->
                when (char) {
                    'L' -> range.lowerHalf
                    'R' -> range.upperHalf
                    else -> throw NotImplementedError()
                }
            }.first
    }
}


private val IntRange.size: Int
    get() = last - first + 1

private val IntRange.lowerHalf: IntRange
    get() = first..last - size / 2

private val IntRange.upperHalf: IntRange
    get() = first + size / 2..last
