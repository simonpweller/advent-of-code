package y2018

import inputText

fun main() {
    val regex = inputText(2018, 20)
    val doorMap = parseExpression(regex)
    println(solve(doorMap))
}

private fun solve(doorMap: Map<Position, Set<Direction>>): Pair<Int, Int> {
    var positions = setOf(Position())
    val visitedPositions = mutableSetOf(Position())
    var steps = 0
    var longPaths = 0
    while (true) {
        val nextPositions = positions.flatMap { position ->
            doorMap.getOrDefault(position, emptySet()).map { position.plus(it) }
        }.filter { it !in visitedPositions }.toSet()
        if (nextPositions.isEmpty()) {
            return Pair(steps, longPaths)
        } else {
            steps++
            visitedPositions.addAll(nextPositions)
            if (steps >= 1000) longPaths += nextPositions.size
            positions = nextPositions
        }
    }
}

private fun parseExpression(regex: String): MutableMap<Position, Set<Direction>> {
    var position = Position()
    val doorMap = mutableMapOf<Position, Set<Direction>>()
    val checkpoints = mutableListOf(Position())
    regex.forEach { char ->
        when (char) {
            '(' -> checkpoints.add(position)
            'N' -> {
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.N)
                position += Direction.N
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.S)
            }
            'E' -> {
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.E)
                position += Direction.E
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.W)
            }
            'S' -> {
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.S)
                position += Direction.S
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.N)
            }
            'W' -> {
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.W)
                position += Direction.W
                doorMap[position] = doorMap.getOrDefault(position, setOf()).plus(Direction.E)
            }
            '|' -> {
                position = checkpoints.last()
            }
            ')' -> {
                checkpoints.removeLast()
            }
        }
    }
    return doorMap
}

data class Position(val x: Int = 0, val y: Int = 0) {
    operator fun plus(direction: Direction) = when(direction) {
        Direction.N -> Position(x, y + 1)
        Direction.E -> Position(x + 1, y)
        Direction.S -> Position(x, y - 1)
        Direction.W -> Position(x - 1, y)
    }
}

enum class Direction { N, E, S, W }
