package y2021
import inputLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val instructions = inputLines(2021, 22).map(::parseInstruction)
    var cuboidsThatAreOn = setOf<Cuboid>()
    instructions.forEach { (type, cuboid) ->
        cuboidsThatAreOn = cuboidsThatAreOn.flatMap { if (it.overlaps(cuboid)) it - cuboid else setOf(it) }.toSet()
        if (type == InstructionType.ON) {
            cuboidsThatAreOn = cuboidsThatAreOn.plus(cuboid)
        }
    }

    val cubesThatAreOn = cuboidsThatAreOn.sumOf { it.size }
    val initializationProcedureRegion = Cuboid(-50 .. 50, -50 .. 50, -50 .. 50)
    val cubesThatAreOnInInitializationProcedureRegion = cubesThatAreOn - cuboidsThatAreOn
        .flatMap { if(it.overlaps(initializationProcedureRegion)) it - initializationProcedureRegion else setOf(it) }.toSet()
        .sumOf { it.size }
    println(cubesThatAreOnInInitializationProcedureRegion)
    println(cubesThatAreOn)
}

private fun parseInstruction(line: String): Instruction {
    val type = if (line.startsWith("on")) InstructionType.ON else InstructionType.OFF
    val ranges = line.substringAfter(" ").split(",")
    return Instruction(type, Cuboid(parseRange(ranges[0]), parseRange(ranges[1]), parseRange(ranges[2])))
}

private fun parseRange(string: String): IntRange = string
    .substring(2, string.length)
    .split("..").map(String::toInt)
    .let { (from, to) -> (from..to) }

private data class Instruction(val type: InstructionType, val cuboid: Cuboid)
private enum class InstructionType { ON, OFF }

private data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    val size: Long
        get() = x.size.toLong() * y.size * z.size
    fun overlaps(other: Cuboid): Boolean = x.overlaps(other.x) && y.overlaps(other.y) && z.overlaps(other.z)
    operator fun minus(other: Cuboid): Set<Cuboid> {
        return setOfNotNull(
            if (other.x.first > x.first) {
                Cuboid(x.first until other.x.first, y, z)
            } else null,
            if (other.y.first > y.first) {
                Cuboid(x.overlapWith(other.x), y.first until other.y.first, z)
            } else null,
            if (other.y.last < y.last) {
                Cuboid(x.overlapWith(other.x), other.y.last + 1 .. y.last, z)
            } else null,
            if (other.z.first > z.first) {
                Cuboid(x.overlapWith(other.x), y.overlapWith(other.y), z.first until other.z.first)
            } else null,
            if (other.z.last < z.last) {
                Cuboid(x.overlapWith(other.x), y.overlapWith(other.y), other.z.last + 1 .. z.last)
            } else null,
            if (other.x.last < x.last) {
                Cuboid(other.x.last + 1 .. x.last, y, z)
            } else null,
        )
    }
}

private fun IntRange.overlaps(other: IntRange): Boolean = first <= other.last && last >= other.first
private fun IntRange.overlapWith(other: IntRange): IntRange = max(first, other.first) .. min(last, other.last)
private val IntRange.size: Int
    get() = last - first + 1
