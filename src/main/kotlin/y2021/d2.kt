package y2021

import inputLines
import java.util.*

fun main() {
    val commands = inputLines(2021, 2).map(::parseCommands)
    println(part1(commands))
    println(part2(commands))
}

private fun parseCommands(input: String): Command = input.split(" ")
    .let { Command(Direction.valueOf(it.first().uppercase(Locale.getDefault())), it.last().toInt()) }

private enum class Direction { FORWARD, UP, DOWN }
private data class Command(val direction: Direction, val distance: Int)

private fun part1(commands: List<Command>): Long {
    var horizontalPosition = 0L
    var depth = 0L

    commands.forEach { (direction, distance) ->
        when (direction) {
            Direction.FORWARD -> horizontalPosition += distance
            Direction.UP -> depth -= distance
            Direction.DOWN -> depth += distance
        }
    }

    return horizontalPosition * depth
}

private fun part2(commands: List<Command>): Long {
    var horizontalPosition = 0L
    var depth = 0L
    var aim = 0L

    commands.forEach { (direction, distance) ->
        when (direction) {
            Direction.FORWARD -> {
                horizontalPosition += distance
                depth += distance * aim
            }
            Direction.UP -> aim -= distance
            Direction.DOWN -> aim += distance
        }
    }

    return horizontalPosition * depth
}
