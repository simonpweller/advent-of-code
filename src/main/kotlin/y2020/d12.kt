package y2020

import inputLines
import kotlin.math.abs

fun main() {
    val evasiveActions = inputLines(2020, 12).map {
        val direction = when (val dirString = it.substring(0, 1)) {
            "N", "S", "E", "W" -> CardinalDirection.valueOf(dirString)
            else -> RelativeDirection.valueOf(dirString)
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


private data class Part1(val position: Position, val facing: CardinalDirection) {
    operator fun plus(evasiveAction: EvasiveAction) = when (evasiveAction.direction) {
        is CardinalDirection -> this.copy(position = position.shift(evasiveAction.direction, evasiveAction.amount))
        RelativeDirection.L, RelativeDirection.R -> this.copy(
            facing = List(evasiveAction.amount / 90) { evasiveAction.direction }.fold(this.facing)
            { acc, curr -> acc + curr as RelativeDirection })
        RelativeDirection.F -> this.copy(position = this.position.shift(facing, evasiveAction.amount))
        else -> throw NotImplementedError()
    }

    val manhattanDistance: Int
        get() = abs(position.x) + abs(position.y)
}

private data class Part2(val position: Position, val waypoint: Position) {
    operator fun plus(evasiveAction: EvasiveAction): Part2 = when (evasiveAction.direction) {
        is CardinalDirection -> this.copy(waypoint = waypoint.shift(evasiveAction.direction, evasiveAction.amount))
        RelativeDirection.L, RelativeDirection.R -> this.copy(
            waypoint = List(evasiveAction.amount / 90) { evasiveAction.direction }.fold(this.waypoint)
                { acc, curr -> acc.rotate(curr as RelativeDirection) })
        RelativeDirection.F -> this.copy(position = Position(this.position.x + waypoint.x * evasiveAction.amount, this.position.y + waypoint.y * evasiveAction.amount))
        else -> throw NotImplementedError()
    }

    val manhattanDistance: Int
        get() = abs(position.x) + abs(position.y)
}

private data class Position(val x: Int, val y: Int) {
    fun shift(cardinalDirection: CardinalDirection, amount: Int) = when (cardinalDirection) {
        CardinalDirection.N -> this.copy(y = y + amount)
        CardinalDirection.E -> this.copy(x = x + amount)
        CardinalDirection.S -> this.copy(y = y - amount)
        CardinalDirection.W -> this.copy(x = x - amount)
    }

    fun rotate(direction: RelativeDirection): Position = when(direction) {
        RelativeDirection.L -> Position(x = -y, y = x)
        RelativeDirection.R -> Position(x= y, y = -x)
        RelativeDirection.F -> throw NotImplementedError()
    }
}

private data class EvasiveAction(val direction: Direction, val amount: Int)

private interface Direction

private enum class CardinalDirection : Direction {
    N,
    E,
    S,
    W;

    operator fun plus(turning: RelativeDirection): CardinalDirection {
        return when (turning) {
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
            RelativeDirection.F -> throw NotImplementedError()
        }
    }
}

private enum class RelativeDirection : Direction {
    L,
    R,
    F,
}
