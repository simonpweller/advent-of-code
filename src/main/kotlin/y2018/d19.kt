package y2018

import inputLines

fun main() {
    val lines = inputLines(2018, 19)
    val instructionPointerRegister = lines.first().substringAfter(" ").toInt()
    val instructions = lines.drop(1).map { line ->
        val (opcode, a, b, c) = line.split(" ")
        Instruction(Opcode.valueOf(opcode.toUpperCase()), a.toInt(), b.toInt(), c.toInt())
    }

    var registers = Registers()
    while (registers.register(instructionPointerRegister) < instructions.size) {
        registers += instructions[registers.register(instructionPointerRegister).toInt()]
        registers = Registers(registers.values.plus(instructionPointerRegister to registers.register(instructionPointerRegister) + 1))
    }
    println(registers.register(0))
}
