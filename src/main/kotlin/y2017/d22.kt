package y2017

import inputLines

fun main() {
    val part1Carrier = VirusCarrier(infectedNodes(inputLines(2017, 22)))
    part1Carrier.burst(10_000)
    println(part1Carrier.infectedCount)

    val part2Carrier = VirusCarrier(infectedNodes(inputLines(2017, 22)))
    part2Carrier.burstPart2(10_000_000)
    println(part2Carrier.infectedCount)
}

fun infectedNodes(rows: List<String>): MutableSet<Point> {
    val rowOffset = (rows.size - 1) / 2
    val colOffset = (rows[0].length - 1) / 2
    return rows
        .flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, c ->
                if (c == '#') Point(colIndex - colOffset, rowIndex - rowOffset) else null
            }
        }
        .toMutableSet()
}

data class VirusCarrier(
    val infectedPoints: MutableSet<Point>,
    val weakenedPoints: MutableSet<Point> = mutableSetOf(),
    val flaggedPoints: MutableSet<Point> = mutableSetOf(),
    var direction: Direction = Direction.UP,
    var position: Point = Point(0, 0),
    var infectedCount: Int = 0
) {
    fun burst() {
        if (infectedPoints.contains(position)) {
            direction += TurnDirection.RIGHT
            infectedPoints.remove(position)
        } else {
            direction += TurnDirection.LEFT
            infectedPoints.add(position)
            infectedCount++
        }
        position += direction
    }

    fun burstPart2() {
        when {
            infectedPoints.contains(position) -> {
                direction += TurnDirection.RIGHT
                infectedPoints.remove(position)
                flaggedPoints.add(position)
            }
            weakenedPoints.contains(position) -> {
                weakenedPoints.remove(position)
                infectedPoints.add(position)
                infectedCount++
            }
            flaggedPoints.contains(position) -> {
                direction += TurnDirection.RIGHT
                direction += TurnDirection.RIGHT
                flaggedPoints.remove(position)
            }
            else -> {
                direction += TurnDirection.LEFT
                weakenedPoints.add(position)
            }
        }
        position += direction
    }

    fun burstPart2(times: Int) {
        repeat(times) { this.burstPart2() }
    }

    fun burst(times: Int) {
        repeat(times) { this.burst() }
    }
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Point = when(direction) {
        Direction.UP -> copy(y = y - 1)
        Direction.RIGHT -> copy(x = x + 1)
        Direction.DOWN -> copy(y = y + 1)
        Direction.LEFT -> copy(x = x - 1)
    }
}

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    operator fun plus(turnDirection: TurnDirection): Direction = when(this) {
        UP -> when(turnDirection) {
            TurnDirection.LEFT -> LEFT
            TurnDirection.RIGHT -> RIGHT
        }
        RIGHT -> when(turnDirection) {
            TurnDirection.LEFT -> UP
            TurnDirection.RIGHT -> DOWN
        }
        DOWN -> when(turnDirection) {
            TurnDirection.LEFT -> RIGHT
            TurnDirection.RIGHT -> LEFT
        }
        LEFT -> when(turnDirection) {
            TurnDirection.LEFT -> DOWN
            TurnDirection.RIGHT -> UP
        }
    }
}

enum class TurnDirection {
    LEFT,
    RIGHT,
}
