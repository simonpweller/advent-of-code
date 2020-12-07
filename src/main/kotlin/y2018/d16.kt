package y2018

import inputChunks

fun main() {
    val chunks = inputChunks(2018, 16)
    val samples = chunks.dropLast(2)

    val part1 = samples.map { sample ->
        Opcode.values().sumBy { opcode ->
            if (behavesLike(opcode, sample)) 1 else 0
        }
    }.count { it >= 3 }

    println(part1)
}

fun behavesLike(opcode: Opcode, sample: String): Boolean {
    val before = parseRegisters(sample.lines().first())
    val instruction = parseInstruction(opcode, sample.lines()[1])
    val after = parseRegisters(sample.lines().last())
    return before + instruction == after
}

fun parseRegisters(line: String): Registers {
    val (a, b, c, d) = "\\[(\\d), (\\d), (\\d), (\\d)]".toRegex().find(line)!!.destructured
    return Registers(mapOf(0 to a.toLong(), 1 to b.toLong(), 2 to c.toLong(), 3 to d.toLong()))
}

fun parseInstruction(opcode: Opcode, line: String): Instruction {
    val (_, a, b, c) = line.split(" ").map(String::toInt)
    return Instruction(opcode, a, b, c)
}

class Instruction(val opcode: Opcode, val a: Int, val b: Int, val c: Int)

enum class Opcode {
    ADDR,
    ADDI,
    MULR,
    MULI,
    BANR,
    BANI,
    BORR,
    BORI,
    SETR,
    SETI,
    GTIR,
    GTRI,
    GTRR,
    EQIR,
    EQRI,
    EQRR,
}

data class Registers(val values: Map<Int, Long>) {
    private fun register(register: Int) = values.getValue(register)
    private fun value(register: Int) = register.toLong()

    operator fun plus(instruction: Instruction): Registers {
        val value = with(instruction) {
            when(opcode) {
                Opcode.ADDR -> register(a) + register(b)
                Opcode.ADDI -> register(a) + value(b)
                Opcode.MULR -> register(a) * register(b)
                Opcode.MULI -> register(a) * value(b)
                Opcode.BANR -> register(a).and(register(b))
                Opcode.BANI -> register(a).and(value(b))
                Opcode.BORR -> register(a).or(register(b))
                Opcode.BORI -> register(a).or(value(b))
                Opcode.SETR -> register(a)
                Opcode.SETI -> value(a)
                Opcode.GTIR -> if(value(a) > register(b)) 1 else 0
                Opcode.GTRI -> if(register(a) > value(b)) 1 else 0
                Opcode.GTRR -> if(register(a) > register(b)) 1 else 0
                Opcode.EQIR -> if(value(a) == register(b)) 1 else 0
                Opcode.EQRI -> if(register(a) == value(b)) 1 else 0
                Opcode.EQRR -> if(register(a) == register(b)) 1 else 0
            }
        }
        return Registers(values.plus(instruction.c to value))
    }
}
