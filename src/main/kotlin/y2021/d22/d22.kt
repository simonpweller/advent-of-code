package y2021.d22
import inputLines

fun main() {
    val instructions = inputLines(2021, 22).map(::parseInstruction)
    val relevantRegion = (-50 .. 50).flatMap { x -> (-50 .. 50).flatMap { y -> (-50 .. 50).map { z -> Point3D(x, y, z) } } }.associateWith { 0 }.toMutableMap()
    instructions.forEach { (type, cube) ->
        relevantRegion.keys.forEach { point ->
            if (point.x in cube.x && point.y in cube.y && point.z in cube.z) {
                relevantRegion[point] = if (type == InstructionType.ON) 1 else 0
            }
        }
    }
    println(relevantRegion.values.sum())
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

private data class Cube(val x: IntRange, val y: IntRange, val z: IntRange)
