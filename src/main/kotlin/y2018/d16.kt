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
    operator fun plus(instruction: Instruction): Registers {
        return when(instruction.opcode) {
            Opcode.ADDR -> Registers(values.plus(instruction.c to values.getValue(instruction.a) + values.getValue(instruction.b)))
            Opcode.ADDI -> Registers(values.plus(instruction.c to values.getValue(instruction.a) + instruction.b))
            Opcode.MULR -> Registers(values.plus(instruction.c to values.getValue(instruction.a) * values.getValue(instruction.b)))
            Opcode.MULI -> Registers(values.plus(instruction.c to values.getValue(instruction.a) * instruction.b))
            Opcode.BANR -> Registers(values.plus(instruction.c to values.getValue(instruction.a).and(values.getValue(instruction.b))))
            Opcode.BANI -> Registers(values.plus(instruction.c to values.getValue(instruction.a).and(instruction.b.toLong())))
            Opcode.BORR -> Registers(values.plus(instruction.c to values.getValue(instruction.a).or(values.getValue(instruction.b))))
            Opcode.BORI -> Registers(values.plus(instruction.c to values.getValue(instruction.a).or(instruction.b.toLong())))
            Opcode.SETR -> Registers(values.plus(instruction.c to values.getValue(instruction.a)))
            Opcode.SETI -> Registers(values.plus(instruction.c to instruction.a.toLong()))
            Opcode.GTIR -> Registers(values.plus(instruction.c to if(instruction.a > values.getValue(instruction.b)) 1 else 0))
            Opcode.GTRI -> Registers(values.plus(instruction.c to if(values.getValue(instruction.a) > instruction.b) 1 else 0))
            Opcode.GTRR -> Registers(values.plus(instruction.c to if(values.getValue(instruction.a) > values.getValue(instruction.b)) 1 else 0))
            Opcode.EQIR -> Registers(values.plus(instruction.c to if(instruction.a.toLong() == values.getValue(instruction.b)) 1 else 0))
            Opcode.EQRI -> Registers(values.plus(instruction.c to if(values.getValue(instruction.a) == instruction.b.toLong()) 1 else 0))
            Opcode.EQRR -> Registers(values.plus(instruction.c to if(values.getValue(instruction.a) == values.getValue(instruction.b)) 1 else 0))
        }
    }
}
