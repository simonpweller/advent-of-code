package y2018

import inputLines

fun main() {
    val lines = inputLines(2018, 19)
    val ipRegister = lines.first().substringAfter(" ").toInt()
    val instructions = lines.drop(1).map(::parseInstruction)
    val result = Cpu(ipRegister).process(instructions)
    println(result)
}

fun parseInstruction(line: String): Instruction {
    val (opcode, a, b, c) = line.split(" ")
    return Instruction(Opcode.valueOf(opcode.toUpperCase()), a.toInt(), b.toInt(), c.toInt())
}

class Cpu (private val ipRegister: Int, initialValueFirstRegister: Long = 0) {
    private var registers = Registers(mapOf(0 to initialValueFirstRegister))
    private val ip: Int
        get() = registers.register(ipRegister).toInt()

    fun process(instructions: List<Instruction>): Long {
        while(ip < instructions.size) {
            process(instructions[ip])
        }
        return registers.values.getValue(0)
    }

    private fun process(instruction: Instruction) {
        registers += instruction
        registers = Registers(registers.values.plus(ipRegister to registers.register(ipRegister) + 1))
    }
}
