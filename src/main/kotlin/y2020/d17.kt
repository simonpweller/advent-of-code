package y2020

import inputLines
import y2020.State.*

fun main() {
    val inputLines = inputLines(2020, 17)
    println(part1(inputLines))
    println(part2(inputLines))
}

private fun part1(inputLines: List<String>): Int {
    var map = listOf(inputLines.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            val x = colIndex - inputLines.size / 2
            val y = rowIndex - row.length / 2
            val z = 0
            Coordinates3D(x, y, z) to if (col == '#') ACTIVE else INACTIVE
        }
    }).flatten().toMap()
    repeat(6) {
        map = run3DCycle(map)
    }
    return map.values.count { it == ACTIVE }
}

private fun part2(inputLines: List<String>): Int {
    var map = listOf(inputLines.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            val x = colIndex - inputLines.size / 2
            val y = rowIndex - row.length / 2
            val z = 0
            val w = 0
            Coordinates4D(x, y, z, w) to if (col == '#') ACTIVE else INACTIVE
        }
    }).flatten().toMap()
    repeat(6) {
        map = run4DCycle(map)
    }
    return map.values.count { it == ACTIVE }
}

private fun run3DCycle(map: Map<Coordinates3D, State>): MutableMap<Coordinates3D, State> {
    val nextMap = map.toMutableMap()
    // Add missing neighbours
    map.keys.forEach { coordinates3D ->
        coordinates3D.neighbours.forEach { neighbour ->
            nextMap.putIfAbsent(neighbour, INACTIVE)
        }
    }
    // Calculate next state
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

private fun run4DCycle(map: Map<Coordinates4D, State>): MutableMap<Coordinates4D, State> {
    val nextMap = map.toMutableMap()
    // Add missing neighbours
    map.keys.forEach { coordinates3D ->
        coordinates3D.neighbours.forEach { neighbour ->
            nextMap.putIfAbsent(neighbour, INACTIVE)
        }
    }
    // Calculate next state
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


private data class Coordinates3D(val x: Int, val y: Int, val z: Int) {
    val neighbours: List<Coordinates3D>
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

private data class Coordinates4D(val x: Int, val y: Int, val z: Int, val w: Int) {
    val neighbours: List<Coordinates4D>
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
