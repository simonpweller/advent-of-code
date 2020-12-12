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
    val status = Status(Position(0, 0), CardinalDirection.E)
    println(evasiveActions.fold(status) { initial, operation ->
        initial + operation
    }.manhattanDistance)
}


private data class Status(val position: Position, val facing: CardinalDirection) {
    operator fun plus(evasiveAction: EvasiveAction): Status = when (evasiveAction.direction) {
        is CardinalDirection -> this.plusCardinalDirection(evasiveAction.direction, evasiveAction.amount)
        RelativeDirection.L, RelativeDirection.R -> this.copy(
            facing = List(evasiveAction.amount / 90) { evasiveAction.direction }.fold(this.facing)
                { acc, curr -> acc + curr as RelativeDirection })
        RelativeDirection.F -> this.plusCardinalDirection(this.facing, evasiveAction.amount)
        else -> throw NotImplementedError()
    }

    fun plusCardinalDirection(cardinalDirection: CardinalDirection, amount: Int): Status = when (cardinalDirection) {
        CardinalDirection.N -> this.copy(position = position.copy(y = this.position.y + amount))
        CardinalDirection.E -> this.copy(position = position.copy(x = this.position.x + amount))
        CardinalDirection.S -> this.copy(position = position.copy(y = this.position.y - amount))
        CardinalDirection.W -> this.copy(position = position.copy(x = this.position.x - amount))
    }

    val manhattanDistance: Int
        get() = abs(position.x) + abs(position.y)
}

private data class Position(val x: Int, val y: Int)

private data class EvasiveAction(val direction: Direction, val amount: Int)

private interface Direction

private enum class CardinalDirection : Direction {
    N,
    E,
    S,
    W, ;

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
