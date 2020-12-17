package y2020

import inputLines
import y2020.State.*

fun main() {
    val inputLines = inputLines(2020, 17)
    var map3D: Map<Coordinates, State> = parseMap3D(inputLines)
    var map4D: Map<Coordinates, State> = parseMap4D(inputLines)
    repeat(6) {
        map3D = runCycle(map3D)
        map4D = runCycle(map4D)
    }
    println(map3D.values.count { it == ACTIVE })
    println(map4D.values.count { it == ACTIVE })
}

private fun parseMap3D(inputLines: List<String>): Map<Coordinates, State> =
    listOf(inputLines.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            val x = colIndex - inputLines.size / 2
            val y = rowIndex - row.length / 2
            val z = 0
            Coordinates3D(x, y, z) to if (col == '#') ACTIVE else INACTIVE
        }
    }).flatten().toMap()

private fun parseMap4D(inputLines: List<String>): Map<Coordinates, State> =
    listOf(inputLines.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            val x = colIndex - inputLines.size / 2
            val y = rowIndex - row.length / 2
            val z = 0
            val w = 0
            Coordinates4D(x, y, z, w) to if (col == '#') ACTIVE else INACTIVE
        }
    }).flatten().toMap()

private fun runCycle(map: Map<Coordinates, State>): MutableMap<Coordinates, State> {
    val nextMap = map.toMutableMap()
    map.keys.forEach { coordinates3D ->
        coordinates3D.neighbours.forEach { neighbour ->
            nextMap.putIfAbsent(neighbour, INACTIVE)
        }
    }
    nextMap.keys.forEach { coordinates3D ->
        val activeNeighbourCount =
            coordinates3D.neighbours.count { neighbour -> map.getOrDefault(neighbour, INACTIVE) == ACTIVE }
        nextMap[coordinates3D] = when (map.getOrDefault(coordinates3D, INACTIVE)) {
            ACTIVE -> if (activeNeighbourCount == 2 || activeNeighbourCount == 3) ACTIVE else INACTIVE
            INACTIVE -> if (activeNeighbourCount == 3) ACTIVE else INACTIVE
        }
    }

    return nextMap
}

private enum class State {
    ACTIVE,
    INACTIVE,
}

private interface Coordinates {
    val neighbours: List<Coordinates>
}

private data class Coordinates3D(val x: Int, val y: Int, val z: Int): Coordinates {
    override val neighbours: List<Coordinates3D>
        get() =
            (-1..1).flatMap { xOffset ->
                (-1..1).flatMap { yOffset ->
                    (-1..1).mapNotNull { zOffset ->
                        if (xOffset == 0 && yOffset == 0 && zOffset == 0) null else Coordinates3D(
                            x + xOffset,
                            y + yOffset,
                            z + zOffset
                        )
                    }
                }
            }
}

private data class Coordinates4D(val x: Int, val y: Int, val z: Int, val w: Int): Coordinates {
    override val neighbours: List<Coordinates4D>
        get() =
            (-1..1).flatMap { xOffset ->
                (-1..1).flatMap { yOffset ->
                    (-1..1).flatMap { zOffset ->
                        (-1..1).mapNotNull { wOffset ->
                            if (xOffset == 0 && yOffset == 0 && zOffset == 0 && wOffset == 0) null else Coordinates4D(
                                x + xOffset,
                                y + yOffset,
                                z + zOffset,
                                w + wOffset,
                            )
                        }
                    }
                }
            }
}
