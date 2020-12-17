package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 5).split(",").map(String::toInt)
    println(IntcodeComputer(intcode, listOf(1)).run())
    println(IntcodeComputer(intcode, listOf(5)).run())
}

private class IntcodeComputer(val intcode: List<Int>, inputs: List<Int>) {
    private val memory = intcode.mapIndexed { index, int -> index to int }.toMap().toMutableMap()
    private val inputStream = inputs.toMutableList()
    private val outputs = mutableListOf<Int>()
    var ip = 0

    fun run(): Int {
        while (ip < intcode.size) {
            when (opcode) {
                1 -> set(3, get(1) + get(2)).also { ip += 4 }
                2 -> set(3, get(1) * get(2)).also { ip += 4 }
                3 -> set(1, inputStream.removeFirst()).also { ip += 2 }
                4 -> outputs.add(get(1)).also { ip += 2 }
                5 -> if (get(1) != 0) ip = get(2) else ip += 3
                6 -> if (get(1) == 0) ip = get(2) else ip += 3
                7 -> set(3, if (get(1) < get(2)) 1 else 0).also { ip += 4 }
                8 -> set(3, if (get(1) == get(2)) 1 else 0).also { ip += 4 }
                99 -> return outputs.last()
            }
        }
        throw IllegalArgumentException("Program didn't terminate")
    }

    private val instruction: String
        get() = memory[ip].toString().padStart(5, '0')

    private val opcode: Int
        get() = instruction.takeLast(2).toInt()

    private fun getMemoryAddress(param: Int): Int =
        if (instruction[3 - param] == '0') memory.getValue(ip + param) else ip + param

    private fun get(param: Int): Int = memory.getOrDefault(getMemoryAddress(param), 0)

    private fun set(param: Int, value: Int) {
        memory[getMemoryAddress(param)] = value
    }
}
