package y2018

import inputLines
import y2018.Cave.Equipment.*
import java.util.*
import kotlin.math.abs

fun main() {
    val input = inputLines(2018, 22)
    val depth = input.first().substringAfter(": ").toInt()
    val (targetCol, targetRow) = input.last().substringAfter(": ").split(",").map { it.toInt() }

    val cave = Cave(depth, targetCol, targetRow)
    println(cave.riskLevel)
    println(cave.minutesToTarget)
}

private data class Cave(val depth: Int, val targetCol: Int, val targetRow: Int) {
    private val target = Coordinate(targetRow, targetCol)
    private val erosionLevels = mutableMapOf<Coordinate, Int>()
    private val regionTypes = mutableMapOf<Coordinate, RegionType>()

    val riskLevel: Int
        get() = (0..target.row).flatMap { row ->
            (0..target.col).map { col ->
                getRegionType(Coordinate(row, col)).riskLevel
            }
        }.sum()

    val minutesToTarget: Int
        get() {
            val seen = mutableSetOf<Pair<Coordinate, Equipment>>()
            val states = DistinctPriorityQueue<State>(compareBy(
                { it.minutes },
                { abs(it.coordinate.row - targetRow) + abs(it.coordinate.col - targetCol) }
            ))
            states.add(State(Coordinate(0, 0), 0, TORCH))
            while (true) {
                val state = states.poll()
                seen.add(Pair(state.coordinate, state.equipment))
                if (state.coordinate == target && state.equipment == TORCH) return state.minutes

                val possibleMoves = (state.equipmentChanges + state.moves)
                    .filter { Pair(it.coordinate, it.equipment) !in seen }
                    .filter { getRegionType(it.coordinate).accessibleWith(it.equipment) }
                states.addAll(possibleMoves)
            }
        }

    private fun getRegionType(coordinate: Coordinate): RegionType {
        if (regionTypes.containsKey(coordinate)) return regionTypes.getValue(coordinate)
        val regionType = when (getErosionLevel(coordinate) % 3) {
            0 -> RegionType.ROCKY
            1 -> RegionType.WET
            else -> RegionType.NARROW
        }
        regionTypes[coordinate] = regionType
        return regionType
    }

    private fun getErosionLevel(coordinate: Coordinate): Int {
        if (erosionLevels.containsKey(coordinate)) return erosionLevels.getValue(coordinate)
        val erosionLevel = erosionLevels[coordinate] ?: with(coordinate) {
            when {
                row == 0 && col == 0 -> 0
                row == target.row && col == target.col -> 0
                row == 0 -> col * 16807
                col == 0 -> row * 48271
                else -> getErosionLevel(Coordinate(row, col - 1)) * getErosionLevel(Coordinate(row - 1, col))
            }.let { geologicIndex -> (geologicIndex + depth) % 20183 }
        }
        erosionLevels[coordinate] = erosionLevel
        return erosionLevel
    }

    private enum class RegionType {
        ROCKY,
        WET,
        NARROW;

        fun accessibleWith(equipment: Equipment): Boolean = when (this) {
            ROCKY -> equipment == CLIMBING_GEAR || equipment == TORCH
            WET -> equipment == CLIMBING_GEAR || equipment == NOTHING
            NARROW -> equipment == TORCH || equipment == NOTHING
        }

        val riskLevel: Int
            get() = when (this) {
                ROCKY -> 0
                WET -> 1
                NARROW -> 2
            }
    }

    private data class Coordinate(val row: Int, val col: Int) {
        val moves: List<Coordinate>
            get() = Direction.values().map { this + it }.filter { it.row >= 0 && it.col >= 0 }

        private operator fun plus(direction: Direction): Coordinate {
            return when (direction) {
                Direction.UP -> this.copy(row = row - 1)
                Direction.RIGHT -> this.copy(col = col + 1)
                Direction.DOWN -> this.copy(row = row + 1)
                Direction.LEFT -> this.copy(col = col - 1)
            }
        }
    }

    private enum class Direction {
        UP, RIGHT, DOWN, LEFT
    }

    private data class State(val coordinate: Coordinate, val minutes: Int, val equipment: Equipment) {
        val equipmentChanges: List<State>
            get() = equipment.changes.map { this.copy(minutes = minutes + 7, equipment = it) }
        val moves: List<State>
            get() = coordinate.moves.map { this.copy(minutes = minutes + 1, coordinate = it) }
    }

    private enum class Equipment {
        CLIMBING_GEAR,
        TORCH,
        NOTHING;

        val changes: List<Equipment>
            get() = when (this) {
                CLIMBING_GEAR -> listOf(TORCH, NOTHING)
                TORCH -> listOf(CLIMBING_GEAR, NOTHING)
                NOTHING -> listOf(TORCH, CLIMBING_GEAR)
            }
    }
}

class DistinctPriorityQueue<E>(comparator: Comparator<E>): PriorityQueue<E>(comparator) {
    override fun addAll(elements: Collection<E>): Boolean {
        return super.addAll(elements.filter { !this.contains(it) })
    }
}

