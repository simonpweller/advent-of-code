package y2021

import combinations
import inputChunks
import subListsOfSize
import kotlin.math.abs

fun main() {
    val scanners = inputChunks(2021, 19).map(Scanner.Companion::of)
    var remainingScanners = scanners.drop(1)
    var rotatedLists = listOf(scanners[0])
    var positions = listOf(Point3D(0, 0, 0))
    while (remainingScanners.isNotEmpty()) {
        val (match, rotation, distance) = rotatedLists.map { findMatchingRotation(it, remainingScanners) }.first { it != null }!!
        positions = positions + distance
        remainingScanners = remainingScanners - remainingScanners[match]
        rotatedLists = rotatedLists + rotation
    }
    println(rotatedLists.map { it.points }.flatten().toSet().size)
    println(subListsOfSize(positions, 2).map { (it.first() - it.last()).let { distance -> abs(distance.x) + abs(distance.y) + abs(distance.z) } }.maxOrNull() )
}

private fun findMatchingRotation(listToMatch: Scanner, candidates: List<Scanner>): Triple<Int, Scanner, Point3D>? {
    candidates.forEachIndexed { index, scanner ->
        val (rotation, distance) = listToMatch.findOverlapWith(scanner) ?: return@forEachIndexed
        return Triple(index, rotation, distance)
    }
    return null
}

private data class Scanner(val points: List<Point3D>) {
    val rotations: List<List<Point3D>> = listOf(
        points.map { (x, y, z) -> Point3D(x, y, z) },
        points.map { (x, y, z) -> Point3D(x, -z, y) },
        points.map { (x, y, z) -> Point3D(x, -y, -z) },
        points.map { (x, y, z) -> Point3D(x, z, -y) },
        points.map { (x, y, z) -> Point3D(-x, -y, z) },
        points.map { (x, y, z) -> Point3D(-x, z, y) },
        points.map { (x, y, z) -> Point3D(-x, y, -z) },
        points.map { (x, y, z) -> Point3D(-x, -z, -y) },
        points.map { (x, y, z) -> Point3D(y, z, x) },
        points.map { (x, y, z) -> Point3D(y, x, -z) },
        points.map { (x, y, z) -> Point3D(y, -z, -x) },
        points.map { (x, y, z) -> Point3D(y, -x, z) },
        points.map { (x, y, z) -> Point3D(-y, x, z) },
        points.map { (x, y, z) -> Point3D(-y, z, -x) },
        points.map { (x, y, z) -> Point3D(-y, -x, -z) },
        points.map { (x, y, z) -> Point3D(-y, -z, x) },
        points.map { (x, y, z) -> Point3D(z, x, y) },
        points.map { (x, y, z) -> Point3D(z, y, -x) },
        points.map { (x, y, z) -> Point3D(z, -x, -y) },
        points.map { (x, y, z) -> Point3D(z, -y, x) },
        points.map { (x, y, z) -> Point3D(-z, y, x) },
        points.map { (x, y, z) -> Point3D(-z, x, -y) },
        points.map { (x, y, z) -> Point3D(-z, -y, -x) },
        points.map { (x, y, z) -> Point3D(-z, -x, y) },
    )

    fun findOverlapWith(other: Scanner): Pair<Scanner, Point3D>? = other.rotations
        .forEach { rotation ->
            val combinations = combinations(this.points, rotation)
            val distances = combinations.map { (a, b) -> a - b }
            val distanceCounts = distances.groupingBy { it }.eachCount()
            val distance = distanceCounts.entries.firstOrNull { it.value == 12 }?.key
            if (distance != null) return Pair(Scanner(rotation.map { it.plus(distance) }), distance)
        }.let { null }

    companion object {
        fun of(input: String) = Scanner(
            input.split(System.lineSeparator()).drop(1)
                .map {
                    it.split(",")
                        .map(String::toInt)
                        .let { coordinates -> Point3D(coordinates[0], coordinates[1], coordinates[2]) }
                }
        )
    }
}

private data class Point3D(val x: Int, val y: Int, val z: Int) {
    operator fun minus(other: Point3D): Point3D = Point3D(this.x - other.x, this.y - other.y, this.z - other.z)
    operator fun plus(other: Point3D): Point3D = Point3D(this.x + other.x, this.y + other.y, this.z + other.z)
}
