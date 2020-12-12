package y2020

import inputLines
import kotlin.math.abs

fun main() {
    val evasiveActions = inputLines(2020, 12).map {
        val direction = when (val dirString = it.substring(0, 1)) {
            "N", "S", "E", "W" -> CardinalDirection.valueOf(dirString)
            "L", "R" -> RelativeDirection.valueOf(dirString)
            else -> Forward.F
        }
        val amount = it.substring(1).toInt()
        EvasiveAction(direction, amount)
    }
    val part1 = evasiveActions.fold(Part1(Position(0, 0), CardinalDirection.E)) { initial, operation ->
        initial + operation
    }.manhattanDistance
    println(part1)

    val part2 = evasiveActions.fold(Part2(Position(0, 0), waypoint = Position(10, 1))) { initial, operation ->
        initial + operation
    }.manhattanDistance
    println(part2)
}

private sealed class Part(val position: Position) {
    val manhattanDistance: Int
        get() = abs(position.x) + abs(position.y)
}

private class Part1(position: Position, val facing: CardinalDirection): Part(position) {
    operator fun plus(evasiveAction: EvasiveAction) = when (evasiveAction.direction) {
        is CardinalDirection -> Part1(position.shift(evasiveAction.direction, evasiveAction.amount), facing)
        is RelativeDirection -> Part1(position, facing.turn(evasiveAction.direction, evasiveAction.amount))
        else -> Part1(position.shift(facing, evasiveAction.amount), facing)
    }
}

private class Part2(position: Position, val waypoint: Position): Part(position) {
    operator fun plus(evasiveAction: EvasiveAction): Part2 = when (evasiveAction.direction) {
        is CardinalDirection -> Part2(position, waypoint.shift(evasiveAction.direction, evasiveAction.amount))
        is RelativeDirection -> Part2(position, waypoint.rotate(evasiveAction.direction, evasiveAction.amount))
        else -> Part2(Position(position.x + waypoint.x * evasiveAction.amount, position.y + waypoint.y * evasiveAction.amount), waypoint)
    }
}

private data class Position(val x: Int, val y: Int) {
    fun shift(cardinalDirection: CardinalDirection, amount: Int) = when (cardinalDirection) {
        CardinalDirection.N -> this.copy(y = y + amount)
        CardinalDirection.E -> this.copy(x = x + amount)
        CardinalDirection.S -> this.copy(y = y - amount)
        CardinalDirection.W -> this.copy(x = x - amount)
    }

    fun rotate(direction: RelativeDirection, degrees: Int): Position {
        if (degrees == 90) return rotate(direction)
        return rotate(direction).rotate(direction, degrees - 90)
    }

    fun rotate(direction: RelativeDirection): Position = when(direction) {
        RelativeDirection.L -> Position(x = -y, y = x)
        RelativeDirection.R -> Position(x= y, y = -x)
    }
}

private data class EvasiveAction(val direction: Direction, val amount: Int)

private interface Direction

private enum class CardinalDirection : Direction {
    N,
    E,
    S,
    W;

    fun turn(direction: RelativeDirection, degrees: Int): CardinalDirection {
        if (degrees == 90) return turn(direction)
        return turn(direction).turn(direction, degrees - 90)
    }

    fun turn(direction: RelativeDirection): CardinalDirection = when (direction) {
        RelativeDirection.L -> when (this) {
            N -> W
            E -> N
            S -> E
            W -> S
        }
        RelativeDirection.R -> when (this) {
            N -> E
            E -> S
            S -> W
            W -> N
        }
    }
}

private enum class RelativeDirection : Direction {
    L,
    R,
}

private enum class Forward: Direction {
    F
}
