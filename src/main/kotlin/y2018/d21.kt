package y2018

import inputLines

fun main() {
    val lines = inputLines(2018, 21)
    val ipRegister = lines.first().substringAfter(" ").toInt()
    val instructions = lines.drop(1).map(::parseInstruction)
    Cpu(ipRegister, 0).process(instructions)
}
