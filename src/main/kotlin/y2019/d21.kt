package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 21).split(",").map(String::toLong)
    println(part1(intcode))
}

private fun part1(intcode: List<Long>): Long {
    val springScript = listOf(
        "NOT A T",
        "NOT B J",
        "OR J T",
        "AND B J",
        "NOT C J",
        "OR J T",
        "AND C J",
        "AND D T",
        "OR T J",
        "WALK",
    ).joinToString("\n").map { it.code.toLong() }.plus(10)

    return IntcodeComputer(intcode, springScript).run().last()
}

