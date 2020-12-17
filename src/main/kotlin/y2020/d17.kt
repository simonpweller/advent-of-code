package y2020

import inputLines
import y2020.State.*

fun main() {
    val inputLines = inputLines(2020, 17)
    println(solve(inputLines) { x, y -> Coordinates3D(x, y, 0) })
    println(solve(inputLines) { x, y -> Coordinates4D(x, y, 0, 0) })
}

private fun solve(inputLines: List<String>, coordinateBuilder: (x: Int, y: Int) -> Coordinates): Int {
    var map = listOf(inputLines.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, col ->
            val x = colIndex - inputLines.size / 2
            val y = rowIndex - row.length / 2
            Pair(coordinateBuilder(x, y), if (col == '#') ACTIVE else INACTIVE)
        }
    }).flatten().toMap()

    repeat(6) {
        map = runCycle(map)
    }

    return map.values.count { it == ACTIVE }
}

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

private data class Coordinates3D(val x: Int, val y: Int, val z: Int) : Coordinates {
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

private data class Coordinates4D(val x: Int, val y: Int, val z: Int, val w: Int) : Coordinates {
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
