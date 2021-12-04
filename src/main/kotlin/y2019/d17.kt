package y2019

import inputText
import y2019.CameraOutput.*
import y2019.Direction.*
import y2019.TurnDirection.*

fun main() {
    val intcode = inputText(2019, 17).split(",").map(String::toLong)
    val output = IntcodeComputer(intcode).run().map(Long::toChar).joinToString("")
    println(output)
    val lines = output.lines()

    val map = mutableMapOf<Coordinate2D, CameraOutput>()

    lines.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, cell ->
            map[Coordinate2D(colIndex, rowIndex)] = when (cell) {
                '#' -> SCAFFOLD
                '.' -> SPACE
                else -> ROBOT
            }
        }
    }

    println(part1(map))
    println(part2Moves(map))
    println(part2(intcode))
}

private fun part2(intcode: List<Long>): Long {
    val moves = "A,C,A,B,A,A,B,C,B,C\n"
    val a = "L,12,L,8,R,12\n"
    val b = "R,12,L,8,L,10\n"
    val c = "L,10,L,8,L,12,R,12\n"
    val config = "n\n"
    val inputs = (moves + a + b + c + config).map { it.toLong() }

    return IntcodeComputer(listOf(2L).plus(intcode.drop(1)), inputs).run().last()
}

private fun part1(map: MutableMap<Coordinate2D, CameraOutput>): Int {
    val intersections =
        map.keys.filter { it.neighbours.all { neighbour -> map.getOrDefault(neighbour, SPACE) == SCAFFOLD } }
    return intersections.sumBy { it.x * it.y }
}

private fun part2Moves(map: Map<Coordinate2D, CameraOutput>): List<Move> {
    var currentPosition = map.entries.find { it.value == ROBOT }!!.key
    var facing = N
    val moves = mutableListOf<Move>()

    while (
        map.getOrDefault(currentPosition.neighbour(facing + L), SPACE) == SCAFFOLD ||
        map.getOrDefault(currentPosition.neighbour(facing + R), SPACE) == SCAFFOLD
    ) {
        val turnDirection = if (map.getOrDefault(currentPosition.neighbour(facing + L), SPACE) == SCAFFOLD) L else R
        facing += turnDirection
        val walkDistance = getWalkDistance(map, currentPosition, facing)
        moves.add(Move(turnDirection, walkDistance))
        currentPosition = currentPosition.walk(facing, walkDistance)
    }
    return moves
}

private fun getWalkDistance(
    map: Map<Coordinate2D, CameraOutput>,
    startingPoint: Coordinate2D,
    direction: Direction
): Int {
    var distance = 0
    var currentPosition = startingPoint
    while (map[currentPosition.neighbour(direction)] == SCAFFOLD) {
        distance++
        currentPosition = currentPosition.neighbour(direction)
    }
    return distance
}

private data class Coordinate2D(val x: Int, val y: Int) {
    val neighbours: List<Coordinate2D>
        get() = listOf(Coordinate2D(x - 1, y), Coordinate2D(x, y - 1), Coordinate2D(x, y + 1), Coordinate2D(x + 1, y))

    fun neighbour(direction: Direction): Coordinate2D = when (direction) {
        N -> Coordinate2D(x, y - 1)
        E -> Coordinate2D(x + 1, y)
        S -> Coordinate2D(x, y + 1)
        W -> Coordinate2D(x - 1, y)
    }

    fun walk(direction: Direction, distance: Int) = when(direction) {
        N -> Coordinate2D(x, y - distance)
        E -> Coordinate2D(x + distance, y)
        S -> Coordinate2D(x, y + distance)
        W -> Coordinate2D(x - distance, y)
    }
}

private enum class Direction {
    N,
    E,
    S,
    W;

    operator fun plus(turnDirection: TurnDirection): Direction = when (turnDirection) {
        L -> when (this) {
            N -> W
            E -> N
            S -> E
            W -> S
        }
        R -> when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
        }
    }
}

private data class Move(val turnDirection: TurnDirection, val walkDistance: Int)

private enum class TurnDirection {
    L,
    R
}

private enum class CameraOutput {
    SCAFFOLD,
    SPACE,
    ROBOT
}
