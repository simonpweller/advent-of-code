package y2020

import inputLines
import kotlin.math.abs

fun main() {
    val evasiveActions = inputLines(2020, 12).map {
        val direction = when (val dirString = it.substring(0, 1)) {
            "N", "S", "E", "W" -> CardinalDirection.valueOf(dirString)
            "L", "R" -> RelativeDirection.valueOf(dirString)
            else -> Forward
        }
        val amount = it.substring(1).toInt()
        EvasiveAction(direction, amount)
    }
    println(Part1().apply { take(evasiveActions) }.manhattanDistance)
    println(Part2().apply { take(evasiveActions) }.manhattanDistance)
}

private sealed class Part(var position: Position = Position(0, 0)) {
    val manhattanDistance: Int
        get() = abs(position.x) + abs(position.y)

    fun take(evasiveActions: List<EvasiveAction>) {
        evasiveActions.forEach { take(it) }
    }
    abstract fun take(evasiveAction: EvasiveAction)
}

private class Part1(var facing: CardinalDirection = CardinalDirection.E): Part() {
    override fun take(evasiveAction: EvasiveAction) = when (evasiveAction.direction) {
        is CardinalDirection -> position = position.shift(evasiveAction.direction, evasiveAction.amount)
        is RelativeDirection -> facing = facing.turn(evasiveAction.direction, evasiveAction.amount)
        else -> position = position.shift(facing, evasiveAction.amount)
    }
}

private class Part2(var waypoint: Position = Position(10, 1)): Part() {
    override fun take(evasiveAction: EvasiveAction) = when (evasiveAction.direction) {
        is CardinalDirection -> waypoint = waypoint.shift(evasiveAction.direction, evasiveAction.amount)
        is RelativeDirection -> waypoint = waypoint.rotate(evasiveAction.direction, evasiveAction.amount)
        else -> position = Position(position.x + waypoint.x * evasiveAction.amount, position.y + waypoint.y * evasiveAction.amount)
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

private object Forward: Direction
