package y2020

import inputLines

fun main() {
    val rowStrings = inputLines(2020, 11)
    val seatingArea = SeatingArea(rowStrings)
    seatingArea.tickTock()
    println(seatingArea.occupiedSeats)
}


private class SeatingArea(rowStrings: List<String>) {
    private var rows: List<List<SeatStatus>> = rowStrings.map { colString -> colString.map(Char::toSeatStatus) }
    private val lastRowIndex = rows.lastIndex
    private val lastColIndex = rows.first().lastIndex

    fun tickTock() {
        while (true) {
            val stable = tick()
            if (stable) return
        }
    }

    private fun tick(): Boolean {
        var stable = true
        rows =
            (0..lastRowIndex).map { rowIndex ->
                (0..lastColIndex).map { colIndex ->
                    val oldStatus = Coordinates(rowIndex, colIndex).seatStatus
                    val newStatus = nextStatus(Coordinates(rowIndex, colIndex))
                    if (oldStatus != newStatus) stable = false
                    newStatus
                }
            }
        return stable
    }

    val occupiedSeats : Int
        get() = rows.sumBy { it.count { seatStatus -> seatStatus == SeatStatus.OCCUPIED } }

    private fun nextStatus(coordinates: Coordinates): SeatStatus {
        val neighbours = listOf(
            Coordinates(-1, -1), Coordinates(-1, 0), Coordinates(-1, 1),
            Coordinates(0, -1), Coordinates(0, 1),
            Coordinates(1, -1), Coordinates(1, 0), Coordinates(1, 1),
        )
            .map { offset -> coordinates + offset }
            .filter { it.col >= 0 && it.row >= 0 }
            .filter { it.row <= lastRowIndex && it.col <= lastColIndex }
            .map(Coordinates::seatStatus)
        return when (coordinates.seatStatus) {
            SeatStatus.FLOOR -> SeatStatus.FLOOR
            SeatStatus.EMPTY -> if (neighbours.any { it == SeatStatus.OCCUPIED }) SeatStatus.EMPTY else SeatStatus.OCCUPIED
            SeatStatus.OCCUPIED -> if (neighbours.count { it == SeatStatus.OCCUPIED } >= 4) SeatStatus.EMPTY else SeatStatus.OCCUPIED
        }
    }

    inner class Coordinates(val row: Int, val col: Int) {
        operator fun plus(offset: Coordinates) = Coordinates(row + offset.row, col + offset.col)
        val seatStatus: SeatStatus
            get() = rows[row][col]

        override fun toString(): String = "row: $row, col: $col"
    }

    override fun toString(): String = rows.joinToString("\n") { row -> row.joinToString("") }.plus("\n")
}

private enum class SeatStatus {
    EMPTY,
    OCCUPIED,
    FLOOR;

    override fun toString(): String = when (this) {
        EMPTY -> "L"
        OCCUPIED -> "#"
        FLOOR -> "."
    }
}

private fun Char.toSeatStatus(): SeatStatus {
    return when (this) {
        'L' -> SeatStatus.EMPTY
        '#' -> SeatStatus.OCCUPIED
        else -> SeatStatus.FLOOR
    }
}
