package y2019

import inputLines
import java.util.*

fun main() {
    val map = inputLines(2019, 18).flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, char ->
            Point(rowIndex, colIndex) to char
        }
    }.toMap()

    val keyPositions = map.mapNotNull { if (it.value in ('a'..'z')) it.value to it.key else null }.toMap()
    val startingPosition = map.entries.first { it.value == '@' }.key
    val startingPoint = StartingPoint(startingPosition, emptySet())
    val queue = PriorityQueue<Path>(compareBy { it.steps })
    queue.add(Path(startingPoint, 0))

    val pathsLengths = mutableListOf<Int>()
    val startingPointsChecked = mutableSetOf<StartingPoint>()
    var maxPathLengthReached = 0

    while (queue.isNotEmpty()) {
        val path = queue.poll()
        if (startingPointsChecked.contains(path.startingPoint)) continue
        startingPointsChecked.add(path.startingPoint)

        val nextPaths = distances(map, path.startingPoint).map {
            Path(StartingPoint(keyPositions.getValue(it.key), path.startingPoint.openedDoors.plus(it.key.toUpperCase())), path.steps + it.value)
        }

        if (path.startingPoint.openedDoors.size == keyPositions.size) pathsLengths.add(path.steps)
        if (path.startingPoint.openedDoors.size > maxPathLengthReached) {
            maxPathLengthReached = path.startingPoint.openedDoors.size
            println(maxPathLengthReached)
        }

        queue.addAll(nextPaths)
    }
    println(pathsLengths.minOrNull())
}

private data class Point(val row: Int, val col: Int) {
    val neighbours: List<Point>
        get() = listOf(Point(row - 1, col), Point(row + 1, col), Point(row, col - 1), Point(row, col + 1))
}

private fun distances(map: Map<Point, Char>, startingPoint: StartingPoint): Map<Char, Int> {
    val queue = PriorityQueue<State>(compareBy { it.steps })
    queue.add(State(startingPoint.position, 0))
    val seen = mutableSetOf(startingPoint.position)
    val distancesTo = mutableMapOf<Char, Int>()
    while (queue.isNotEmpty()) {
        val state = queue.poll()
        val char = map.getValue(state.position)
        if (char in ('a' .. 'z') && distancesTo[char] == null) {
            distancesTo[char] = state.steps
        }
        val next = state.position.neighbours.filter { it !in seen && map[it] != '#' && map[it] !in ('A'..'Z').minus(startingPoint.openedDoors) }
        seen.addAll(state.position.neighbours)
        queue.addAll(next.map { State(it, state.steps + 1) })
    }
    return distancesTo
}

private data class StartingPoint(val position: Point, val openedDoors: Set<Char>)
private data class State(val position: Point, val steps: Int)
private data class Path(val startingPoint: StartingPoint, val steps: Int)
private data class DistanceSpec(val points: Set<Point>, val openedDoors: Set<Char>)
