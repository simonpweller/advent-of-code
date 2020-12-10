package y2018

import inputLines

fun main() {
    val input = inputLines(2018, 22)
    val depth = input.first().substringAfter(": ").toInt()
    val (targetCol, targetRow) = input.last().substringAfter(": ").split(",").map { it.toInt() }

    println(Cave(depth, targetCol, targetRow).riskLevel)
}

private data class Cave(val depth: Int, val targetCol: Int, val targetRow: Int) {
    private val erosionLevels = mutableMapOf<Coordinate, Int>()
    private val regionTypes = mutableMapOf<Coordinate, RegionType>()

    val riskLevel: Int
        get() = (0..targetRow).flatMap { row ->
            (0..targetCol).map { col ->
                getRegionType(Coordinate(row, col)).riskLevel
            }
        }.sum()

    private fun getRegionType(coordinate: Coordinate): RegionType {
        if (regionTypes.containsKey(coordinate)) return regionTypes.getValue(coordinate)
        val regionType = when (getErosionLevel(coordinate) % 3) {
            0 -> RegionType.ROCKY
            1 -> RegionType.WET
            else -> RegionType.NARROW
        }
        regionTypes[coordinate] = regionType
        return regionType
    }

    private fun getErosionLevel(coordinate: Coordinate): Int {
        if (erosionLevels.containsKey(coordinate)) return erosionLevels.getValue(coordinate)
        val erosionLevel = erosionLevels[coordinate] ?: with(coordinate) {
            when {
                row == 0 && col == 0 -> 0
                row == targetRow && col == targetCol -> 0
                row == 0 -> col * 16807
                col == 0 -> row * 48271
                else -> getErosionLevel(Coordinate(row, col - 1)) * getErosionLevel(Coordinate(row - 1, col))
            }.let { geologicIndex -> (geologicIndex + depth) % 20183 }
        }
        erosionLevels[coordinate] = erosionLevel
        return erosionLevel
    }

    private enum class RegionType {
        ROCKY,
        WET,
        NARROW;

        val riskLevel: Int
            get() = when (this) {
                ROCKY -> 0
                WET -> 1
                NARROW -> 2
            }
    }

    private data class Coordinate(val row: Int, val col: Int)
}

