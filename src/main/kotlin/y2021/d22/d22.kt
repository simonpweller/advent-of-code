package y2021.d22
import inputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val instructions = inputLines(2021, 22).map(::parseInstruction)
    var cubesThatAreOn = setOf<Cube>()
    instructions.forEach { (type, cube) ->
        cubesThatAreOn = cubesThatAreOn.flatMap { if (it.overlaps(cube)) it - cube else setOf(it) }.toSet()
        if (type == InstructionType.ON) {
            cubesThatAreOn = cubesThatAreOn.plus(cube)
        }
    }
    println(cubesThatAreOn.sumOf { it.size })
}

private fun parseInstruction(line: String): Instruction {
    val type = if (line.startsWith("on")) InstructionType.ON else InstructionType.OFF
    val ranges = line.substringAfter(" ").split(",")
    return Instruction(type, Cube(parseRange(ranges[0]), parseRange(ranges[1]), parseRange(ranges[2])))
}

private fun parseRange(string: String): IntRange = string
    .substring(2, string.length)
    .split("..").map(String::toInt)
    .let { (from, to) -> (from..to) }

private data class Instruction(val type: InstructionType, val cube: Cube)
private enum class InstructionType { ON, OFF }
private data class Point3D(val x: Int, val y: Int, val z: Int)

private data class Cube(val x: IntRange, val y: IntRange, val z: IntRange) {
    val size: Long
        get() = x.size.toLong() * y.size * z.size
    fun overlaps(other: Cube): Boolean = x.overlaps(other.x) && y.overlaps(other.y) && z.overlaps(other.z)
    operator fun minus(other: Cube): Set<Cube> {
        return setOfNotNull(
            if (other.x.first > x.first) {
                Cube(x.first until other.x.first, y, z)
            } else null,
            if (other.y.first > y.first) {
                Cube(max(x.first, other.x.first) .. min(x.last, other.x.last), y.first until other.y.first, z)
            } else null,
            if (other.y.last < y.last) {
                Cube(max(x.first, other.x.first) .. min(x.last, other.x.last), other.y.last + 1 .. y.last, z)
            } else null,
            if (other.z.first > z.first) {
                Cube(max(x.first, other.x.first) .. min(x.last, other.x.last), max(y.first, other.y.first) .. min(y.last, other.y.last), z.first until other.z.first)
            } else null,
            if (other.z.last < z.last) {
                Cube(max(x.first, other.x.first) .. min(x.last, other.x.last), max(y.first, other.y.first) .. min(y.last, other.y.last), other.z.last + 1 .. z.last)
            } else null,
            if (other.x.last < x.last) {
                Cube(other.x.last + 1 .. x.last, y, z)
            } else null,
        )
    }
}

private fun IntRange.overlaps(other: IntRange): Boolean = first <= other.last && last >= other.first
private fun IntRange.overlapWith(other: IntRange): IntRange = max(first, other.first) .. min(last, other.last)
private val IntRange.size: Int
    get() = last - first + 1
