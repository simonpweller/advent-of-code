package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 5).split(",").map(String::toInt)
    println(IntcodeComputer(intcode, listOf(1)).run())
}

class IntcodeComputer(intcode: List<Int>, inputs: List<Int>) {
    private val memory = intcode.toMutableList()
    private val inputStream = inputs.toMutableList()
    private val outputs = mutableListOf<Int>()
    var ip = 0

    fun run(): Int {
        while (ip < memory.size) {

            when (opcode) {
                1 -> {
                    memory[param3Addr] = (memory[param1Addr] + memory[param2Addr])
                    ip += 4
                }
                2 -> {
                    memory[param3Addr] = (memory[param1Addr] * memory[param2Addr])
                    ip += 4
                }
                3 -> {
                    memory[param1Addr] = inputStream.removeFirst()
                    ip += 2
                }
                4 -> {
                    outputs.add(memory[param1Addr])
                    ip += 2
                }
                99 -> {
                    return outputs.last()
                }
            }
        }
        throw IllegalArgumentException("Program didn't terminate")
    }

    fun write(value: Int) {
        memory[memory[ip + 3]] = value
    }

    private val instruction: String
        get() = memory[ip].toString().padStart(5, '0')

    private val opcode: Int
        get() = instruction.takeLast(2).toInt()

    private val param1Addr: Int
        get() {
            val mode = instruction[2]
            return if (mode == '0') memory[ip + 1] else ip + 1
        }

    private val param2Addr: Int
        get() {
            val mode = instruction[1]
            return if (mode == '0') memory[ip + 2] else ip + 2
        }

    private val param3Addr: Int
        get() {
            val mode = instruction[0]
            return if (mode == '0') memory[ip + 3] else ip + 3
        }
}
