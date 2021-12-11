package y2019

import inputLines
import java.util.*

fun main() {
    val input = inputLines(2019, 20)
    val labelMap = getLabelMap(input)
    val connections = mutableMapOf<Coordinate, Coordinate>()
    labelMap.filter { it.value.size == 2 }.forEach { (_, value) ->
        connections[value.first()] = value.last()
        connections[value.last()] = value.first()
    }

    val startingPoint = labelMap.getValue("AA").first()
    val destination = labelMap.getValue("ZZ").first()
    println(part1(startingPoint, destination, input, connections))
    println(part2(startingPoint, destination, input, connections))
}

private fun part2(
    startingPoint: Coordinate,
    destination: Coordinate,
    input: List<String>,
    connections: MutableMap<Coordinate, Coordinate>
): Int {
    val seen = mutableSetOf(Pair(startingPoint, 0))
    val queue = LinkedList<Triple<Int, Coordinate, Int>>().also { it.add(Triple(0, startingPoint, 0)) }
    val rightEdgeColIndex = connections.map { it.value.x }.maxOrNull()
    val bottomEdgeRowIndex = connections.map { it.value.y }.maxOrNull()

    while (true) {
        val (steps, position, level) = queue.poll()
        if (position == destination && level == 0) {
            return steps
        }
        val next = position.neighbours
            .filter { input[it.y][it.x] == '.' }
            .map { Pair(it, level) }
            .plus(connections.getOrDefault(position, null)?.let {
                val goingOut = position.x == 2 || position.y == 2 || position.x == rightEdgeColIndex || position.y == bottomEdgeRowIndex
                when {
                    goingOut && level == 0 -> null
                    goingOut -> Pair(it, level - 1)
                    else -> Pair(it, level + 1)
                }
            })
            .filterNotNull()
            .filterNot { seen.contains(it) }
        seen.addAll(next)
        queue.addAll(next.map { Triple(steps + 1, it.first, it.second) })
    }
}

private fun part1(
    startingPoint: Coordinate,
    destination: Coordinate,
    input: List<String>,
    connections: MutableMap<Coordinate, Coordinate>
): Int {
    val seen = mutableSetOf(startingPoint)
    val queue = LinkedList<Pair<Int, Coordinate>>().also { it.add(Pair(0, startingPoint)) }

    while (true) {
        val (steps, position) = queue.poll()
        if (position == destination) {
            return steps
        }
        val next = position.neighbours
            .filter { input[it.y][it.x] == '.' }
            .plus(connections.getOrDefault(position, null))
            .filterNotNull()
            .filterNot { seen.contains(it) }
        seen.addAll(next)
        queue.addAll(next.map { Pair(steps + 1, it) })
    }
}

private data class Coordinate(val x: Int, val y: Int) {
    val neighbours: List<Coordinate>
        get() = listOf(Coordinate(x - 1, y), Coordinate(x + 1, y), Coordinate(x, y - 1), Coordinate(x, y + 1))
}

private fun getLabelMap(input: List<String>): Map<String, List<Coordinate>> {
    val thickness = input[input.size / 2].count { setOf('#', '.').contains(it) } / 2
    val donutWidth = input[2].count { setOf('#', '.').contains(it) }
    val colIndexRange = (2 until 2 + donutWidth)
    val innerColIndexRange = (colIndexRange.first + thickness..colIndexRange.last - thickness)
    val rowIndexRange = (2 until input.size - 2)
    val innerRowIndexRange = (rowIndexRange.first + thickness..rowIndexRange.last - thickness)
    val topLabels = colIndexRange.filter { col -> input[2][col] == '.' }.map { col ->
        listOf(input[0][col], input[1][col]).joinToString("") to Coordinate(col, 2)
    }
    val leftLabels = rowIndexRange.filter { row -> input[row][2] == '.' }.map { row ->
        listOf(input[row][0], input[row][1]).joinToString("") to Coordinate(2, row)
    }
    val lastColumnIndex = colIndexRange.last
    val rightLabels = rowIndexRange.filter { row -> input[row].getOrNull(lastColumnIndex) == '.' }.map { row ->
        listOf(input[row][lastColumnIndex + 1], input[row][lastColumnIndex + 2]).joinToString("") to Coordinate(lastColumnIndex, row)
    }
    val lastRowIndex = rowIndexRange.last
    val bottom = colIndexRange.filter { col -> input[lastRowIndex][col] == '.' }.map { col ->
        listOf(input[lastRowIndex + 1][col], input[lastRowIndex + 2][col]).joinToString("") to Coordinate(col, lastRowIndex)
    }
    val insideTop = innerColIndexRange.filter { col -> input[1 + thickness][col] == '.' }.map { col ->
        listOf(input[2 + thickness][col], input[3 + thickness][col]).joinToString("") to Coordinate(col, 1 + thickness)
    }
    val insideLeft = innerRowIndexRange.filter { row -> input[row][1 + thickness] == '.' }.map { row ->
        listOf(input[row][2 + thickness], input[row][3 + thickness]).joinToString("") to Coordinate(1 + thickness, row)
    }
    val insideRight = innerRowIndexRange.filter { row -> input[row][lastColumnIndex - thickness + 1] == '.' }.map { row ->
            listOf(input[row][lastColumnIndex - thickness - 1], input[row][lastColumnIndex - thickness]).joinToString("") to Coordinate(lastColumnIndex - thickness + 1, row)
        }
    val insideBottom = innerColIndexRange.filter { col -> input[lastRowIndex - thickness + 1][col] == '.' }.map { col ->
            listOf(input[lastRowIndex - thickness - 1][col], input[lastRowIndex - thickness][col]).joinToString("") to Coordinate(col, lastRowIndex - thickness + 1)
        }

    val labels = topLabels + leftLabels + rightLabels + bottom + insideTop + insideLeft + insideRight + insideBottom
    return labels.groupBy { it.first }.mapValues { entry -> entry.value.map { it.second } }
}
