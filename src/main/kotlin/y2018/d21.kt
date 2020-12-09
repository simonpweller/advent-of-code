package y2018

import inputLines

fun main() {
    val lines = inputLines(2018, 21)
    val ipRegister = lines.first().substringAfter(" ").toInt()
    val instructions = lines.drop(1).map(::parseInstruction)
    Cpu2(ipRegister, 0).process(instructions)
}

class Cpu2 (private val ipRegister: Int, initialValueFirstRegister: Long = 0) {
    private var registers = Registers(mapOf(0 to initialValueFirstRegister))
    private val ip: Int
        get() = registers.register(ipRegister).toInt()

    fun process(instructions: List<Instruction>): Long? {
        val seen = mutableSetOf<Long>()
        while(ip < instructions.size) {
            if (ip == 18) { // short-circuit inner loop
                registers = Registers(registers.values.plus(5 to registers.register(4) / 256).plus(2 to 26L).plus(1 to 1L))
            }
            if (ip == 28) { // would exit if register 0 == register 3
                if (registers.register(3) in seen) {
                    println(seen.first())
                    println(seen.last())
                    return null
                }
                seen.add(registers.register(3))
            }
            process(instructions[ip])
        }
        return registers.values.getValue(0)
    }

    private fun process(instruction: Instruction) {
        registers += instruction
        registers = Registers(registers.values.plus(ipRegister to registers.register(ipRegister) + 1))
    }
}
