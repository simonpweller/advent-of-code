package y2019

import inputText
import y2019.CameraOutput.*

fun main() {
    val intcode = inputText(2019, 17).split(",").map(String::toLong)
    val output = IntcodeComputer(intcode).run().map(Long::toChar).joinToString("")
    println(output)
    val lines = output.lines()

    val map = mutableMapOf<Coordinate2D, CameraOutput>()

    lines.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, cell ->
            map[Coordinate2D(colIndex, rowIndex)] = if (cell == '#') SCAFFOLD else SPACE
        }
    }

    val intersections =
        map.keys.filter { it.neighbours.all { neighbour -> map.getOrDefault(neighbour, SPACE) == SCAFFOLD } }
    val alignmentParameterSum = intersections.sumBy { it.x * it.y }
    println(alignmentParameterSum)
}

private data class Coordinate2D(val x: Int, val y: Int) {
    val neighbours: List<Coordinate2D>
        get() = listOf(Coordinate2D(x - 1, y), Coordinate2D(x, y - 1), Coordinate2D(x, y + 1), Coordinate2D(x + 1, y))
}

private enum class CameraOutput {
    SCAFFOLD,
    SPACE,
    ROBOT
}
