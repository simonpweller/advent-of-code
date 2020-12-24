package y2020

import inputLines
import y2020.Color.BLACK
import y2020.Color.WHITE
import y2020.HexDirection.*

fun main() {
    var colorMap = getColorMap()
    println(colorMap.values.filter { it == BLACK }.size)

    repeat(100) {
        val newCoordinatesWithMultipleNeighbours =
            colorMap.keys.flatMap { it.neighbours }.fold(mapOf<Coordinate, Int>()) { counts, coordinate ->
                counts.plus(coordinate to counts.getOrDefault(coordinate, 0) + 1)
            }.filter { entry -> entry.value > 1 }.keys
        val coordinatesToCheck = colorMap.keys + newCoordinatesWithMultipleNeighbours
        colorMap = coordinatesToCheck.fold(mapOf()) { map, coordinate ->
            val adjacentBlackTiles = coordinate.neighbours.count { colorMap.getOrDefault(it, WHITE) == BLACK }
            val nextColor = when (colorMap.getOrDefault(coordinate, WHITE)) {
                BLACK -> if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2) WHITE else BLACK
                WHITE -> if (adjacentBlackTiles == 2) BLACK else WHITE
            }
            map.plus(coordinate to nextColor)
        }
    }
    println(colorMap.values.filter { it == BLACK }.size)
}

private fun getColorMap(): Map<Coordinate, Color> {
    val instructions = inputLines(2020, 24).map { str ->
        str.replace("e", "e ").replace("w", "w ").trim().toUpperCase().split(" ")
            .map { HexDirection.valueOf(it) }
    }

    val locations = instructions.map { instruction ->
        instruction.fold(
            Coordinate(
                0,
                0,
                0
            )
        ) { coordinate, hexDirection -> coordinate + hexDirection }
    }
    return locations.fold(mapOf()) { map, coordinate ->
        map.plus<Coordinate, Color>(
            coordinate to !map.getOrDefault(
                coordinate,
                WHITE
            )
        )
    }
}

private data class Coordinate(val x: Int, val y: Int, val z: Int) {

    operator fun plus(hexDirection: HexDirection): Coordinate {
        return when (hexDirection) {
            E -> Coordinate(x + 1, y - 1, z)
            SE -> Coordinate(x, y - 1, z + 1)
            NE -> Coordinate(x + 1, y, z - 1)
            W -> Coordinate(x - 1, y + 1, z)
            NW -> Coordinate(x, y + 1, z - 1)
            SW -> Coordinate(x - 1, y, z + 1)
        }
    }

    val neighbours: Set<Coordinate>
        get() = HexDirection.values().map { this + it }.toSet()
}

private enum class HexDirection {
    E, SE, NE, W, NW, SW
}

private enum class Color {
    BLACK, WHITE;

    operator fun not() = when (this) {
        BLACK -> WHITE
        WHITE -> BLACK
    }
}
