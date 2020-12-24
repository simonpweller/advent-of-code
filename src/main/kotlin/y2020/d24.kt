package y2020

import inputLines
import y2020.Color.BLACK
import y2020.Color.WHITE
import y2020.HexDirection.*

fun main() {
    val colorMap = getColorMap()
    println(colorMap.values.filter { it == BLACK }.size)
}

private fun getColorMap(): Map<Coordinate, Color> {
    val instructions = inputLines(2020, 24).map { str ->
        var prev: String? = null
        val directions = mutableListOf<HexDirection>()
        str.map { it.toString().toUpperCase() }.forEach { char ->
            if (char == "N" || char == "S") {
                prev = char
            } else {
                if (prev == null) {
                    directions.add(valueOf(char))
                } else {
                    directions.add(valueOf(prev + char))
                    prev = null
                }
            }
        }
        directions
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
    val colorMap = locations.fold(mapOf<Coordinate, Color>()) { map, coordinate ->
        map.plus(
            coordinate to !map.getOrDefault(
                coordinate,
                WHITE
            )
        )
    }
    return colorMap
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
}

private enum class HexDirection {
    E, SE, NE, W, NW, SW
}

private enum class Color {
    BLACK, WHITE;

    operator fun not() = when(this) {
        BLACK -> WHITE
        WHITE -> BLACK
    }
}
