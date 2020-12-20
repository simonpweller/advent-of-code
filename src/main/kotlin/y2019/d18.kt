package y2019

import inputLines
import java.util.*

fun main() {
    val map = inputLines(2019, 18).flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, char ->
            Point(rowIndex, colIndex) to char
        }
    }.toMap()

    val doorPositions = map.mapNotNull { if (it.value in ('A'..'Z')) it.value to it.key else null }.toMap()
    val keyPositions = map.mapNotNull { if (it.value in ('a'..'z')) it.value to it.key else null }.toMap()
    val completePaths = mutableListOf<List<Char>>()

    val queue = PriorityQueue<List<Char>>(compareBy { 1 })
    queue.add(emptyList())
    while (queue.isNotEmpty()) {
        val path = queue.poll()
        val openedDoors = path.mapNotNull { doorPositions.getOrDefault(it.toUpperCase(), null) }
        val usedKeys = path.map { keyPositions.getValue(it) }
        val currentMap = map.plus(openedDoors.plus(usedKeys).map { it to '.' })
        val keysInRange = keysInRange(currentMap)
        if (keysInRange.isEmpty()) completePaths.add(path) else queue.addAll(keysInRange.map { path.plus(it) })
    }

    println(completePaths.size)

    val steps = completePaths.map { path ->
        var steps = 0
        var startingPosition = map.filter { it.value == '@' }.keys.first()
        val currentMap = map.toMutableMap()
        path.forEach { key ->
            steps += stepsTo(currentMap, keyPositions.getValue(key))
            val doorPosition = doorPositions.getOrDefault(key.toUpperCase(), null)
            if (doorPosition != null) currentMap[doorPosition] = '.'
            currentMap[keyPositions.getValue(key)] = '.'
            currentMap[startingPosition] = '.'
            currentMap[keyPositions.getValue(key)] = '@'
            startingPosition = keyPositions.getValue(key)
        }
        steps
    }
    println(steps.minOrNull())
}

private data class Point(val row: Int, val col: Int) {
    val neighbours: List<Point>
        get() = listOf(Point(row - 1, col), Point(row + 1, col), Point(row, col - 1), Point(row, col + 1))
}

private fun keysInRange(map: Map<Point, Char>): List<Char> {
    val startingPosition = map.filter { it.value == '@' }.keys.first()
    val queue = PriorityQueue<Point>(compareBy { 1 })
    queue.add(startingPosition)
    val seen = mutableSetOf(startingPosition)
    while (queue.isNotEmpty()) {
        val point = queue.poll()
        val next = point.neighbours.filter { it !in seen && map[it] != '#' && map[it] !in ('A'..'Z') && map[it] !in ('a' .. 'z') }
        seen.addAll(point.neighbours)
        queue.addAll(next)
    }
    return seen.filter { map[it] in ('a'..'z') }.map { map.getValue(it) }
}

private fun stepsTo(map: Map<Point, Char>, to: Point): Int {
    val startingPosition = map.filter { it.value == '@' }.keys.first()
    val queue = PriorityQueue<State>(compareBy { 1 })
    queue.add(State(startingPosition, 0))
    val seen = mutableSetOf(startingPosition)
    while (queue.peek().position != to) {
        val state = queue.poll()
        val next = state.position.neighbours.filter { it !in seen && map[it] != '#' && map[it] !in ('A'..'Z') && map[it] !in ('a' .. 'z').minus(map[to]) }
        seen.addAll(state.position.neighbours)
        queue.addAll(next.map { State(it, state.steps + 1) })
    }
    return queue.poll().steps
}

private data class State(val position: Point, val steps: Int)

