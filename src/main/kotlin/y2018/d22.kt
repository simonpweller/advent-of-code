package y2018

import inputLines

fun main() {
    val input = inputLines(2018, 22)
    val depth = input.first().substringAfter(": ").toInt()
    val (targetCol, targetRow) = input.last().substringAfter(": ").split(",").map { it.toInt() }

    val map = mutableMapOf<Coordinate, Int>()

    (0..targetRow).forEach { row ->
        (0..targetCol).forEach { col ->
            val geologicIndex = when {
                row == 0 && col == 0 -> 0
                row == targetRow && col == targetCol -> 0
                row == 0 -> col * 16807
                col == 0 -> row * 48271
                else -> map.getValue(Coordinate(row, col - 1)) * map.getValue(Coordinate(row - 1, col))
            }
            map[Coordinate(row, col)] = (geologicIndex + depth) % 20183
        }
    }

    val cave = (0..targetRow).map { row ->
        (0..targetCol).map { col ->
            when (map.getValue(Coordinate(row, col)) % 3) {
                0 -> RegionType.ROCKY
                1 -> RegionType.WET
                else -> RegionType.NARROW
            }
        }
    }

    printCave(cave)
    println(getRiskLevel(cave))
}

private fun getRiskLevel(cave: List<List<RegionType>>) =
    cave.flatMap { row ->
        row.map {
            when (it) {
                RegionType.ROCKY -> 0
                RegionType.WET -> 1
                RegionType.NARROW -> 2
            }
        }
    }.sum()

private fun printCave(cave: List<List<RegionType>>) {
    cave.forEach { row ->
        println(row.joinToString("") {
            when (it) {
                RegionType.ROCKY -> "."
                RegionType.WET -> "="
                RegionType.NARROW -> "|"
            }
        })
    }
}

private enum class RegionType {
    ROCKY,
    WET,
    NARROW
}

private data class Coordinate(val row: Int, val col: Int)
