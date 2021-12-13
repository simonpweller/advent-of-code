package y2021

import inputLines
import java.util.*

fun main() {
    val input = inputLines(2021, 12)
    println(solve(getConnections(input), useJoker = false))
    println(solve(getConnections(input), useJoker = true))
}

private fun solve(connections: Map<String, Set<String>>, useJoker: Boolean): Int {
    val queue = LinkedList<Triple<String, Set<String>, Boolean>>()
    queue.add(Triple("start", setOf("start"), useJoker))
    var pathCount = 0
    while (queue.isNotEmpty()) {
        val (location, seen, jokerAvailable) = queue.poll()
        if (location == "end") {
            pathCount++
        } else {
            val nextLocations = connections.getValue(location)
                .filter { it != "start" && (!seen.contains(it) || jokerAvailable) }
            queue.addAll(nextLocations.map { nextLocation ->
                when {
                    nextLocation.first().isUpperCase() -> Triple(nextLocation, seen, jokerAvailable)
                    seen.contains(nextLocation) -> Triple(nextLocation, seen, false)
                    else -> Triple(nextLocation, seen.plus(nextLocation), jokerAvailable)
                }
            })
        }
    }
    return pathCount
}

private fun getConnections(input: List<String>): Map<String, Set<String>> = input.map { it.split("-") }
    .fold(mapOf()) { map, (left, right) ->
        map
            .plus(left to map.getOrDefault(left, emptySet()).plus(right))
            .plus(right to map.getOrDefault(right, emptySet()).plus(left))
    }
