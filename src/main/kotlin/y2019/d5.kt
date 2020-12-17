package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 5).split(",").map(String::toLong)
    println(IntcodeComputer(intcode, listOf(1)).run().last())
    println(IntcodeComputer(intcode, listOf(5)).run().last())
}

class IntcodeComputer(private val intcode: List<Long>, inputs: List<Long>) {
    private val memory = intcode.mapIndexed { index, int -> index.toLong() to int }.toMap().toMutableMap()
    private val inputStream = inputs.toMutableList()
    private val outputs = mutableListOf<Long>()
    var ip = 0L
    var relativeBase = 0L

    fun run(): List<Long> {
        while (ip < intcode.size) {
            when (opcode) {
                1L -> set(3, get(1) + get(2)).also { ip += 4 }
                2L -> set(3, get(1) * get(2)).also { ip += 4 }
                3L -> set(1, inputStream.removeFirst()).also { ip += 2 }
                4L -> outputs.add(get(1)).also { ip += 2 }
                5L -> if (get(1) != 0L) ip = get(2) else ip += 3
                6L -> if (get(1) == 0L) ip = get(2) else ip += 3
                7L -> set(3, if (get(1) < get(2)) 1 else 0).also { ip += 4 }
                8L -> set(3, if (get(1) == get(2)) 1 else 0).also { ip += 4 }
                9L -> relativeBase += get(1).also { ip += 2 }
                99L -> return outputs
            }
        }
        throw IllegalArgumentException("Program didn't terminate")
    }

    private val instruction: String
        get() = memory[ip].toString().padStart(5, '0')

    private val opcode: Long
        get() = instruction.takeLast(2).toLong()

    private fun getMemoryAddress(param: Long): Long =
        when(instruction[(3 - param).toInt()]) {
            '0' -> memory.getOrDefault(ip + param, 0)
            '1' -> ip + param
            else -> memory.getOrDefault(ip + param, 0) + relativeBase
        }

    private fun get(param: Long): Long {
        return memory.getOrDefault(getMemoryAddress(param), 0)
    }

    private fun set(param: Long, value: Long) {
        memory[getMemoryAddress(param)] = value
    }
}
