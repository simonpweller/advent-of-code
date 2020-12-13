package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 2).split(",").map(String::toInt)
    println(part1(intcode, 12, 2))
    println(part2(intcode))
}

private fun part2(intcode: List<Int>): Int {
    (0 .. 99).forEach { noun ->
        (0 .. 99).forEach { verb ->
            if (part1(intcode, noun, verb) == 19690720) return 100 * noun + verb
        }
    }
    throw IllegalArgumentException("No solution found")
}

private fun part1(intcode: List<Int>, noun: Int, verb: Int): Int {
    val memory = intcode.toMutableList()
    memory[1] = noun
    memory[2] = verb
    (0..memory.size - 4 step 4).forEach { index ->
        val (opcode, arg1, arg2, target) = memory.subList(index, index + 4)
        when (opcode) {
            1 -> memory[target] = memory[arg1] + memory[arg2]
            2 -> memory[target] = memory[arg1] * memory[arg2]
            99 -> return memory[0]
        }
    }
    throw IllegalArgumentException("Program didn't terminate")
}
