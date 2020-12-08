package y2020

import inputLines
import java.lang.IllegalStateException

fun main() {
    val instructions = inputLines(2020, 8).map {
        val (opcode, value) = it.split(" ")
        Instruction(Opcode.valueOf(opcode.toUpperCase()), value.toInt())
    }

    println(part1(instructions))
    println(part2(instructions))
}

private fun part1(instructions: List<Instruction>) = run(instructions).accumulator
private fun part2(instructions: List<Instruction>): Int {
    instructions.forEachIndexed { idx, instruction ->
        if (instruction.opcode != Opcode.ACC) {
            val modifiedInstructions = instructions.toMutableList()
            modifiedInstructions[idx] =
                Instruction(if (instruction.opcode == Opcode.NOP) Opcode.JMP else Opcode.NOP, instruction.value)
            val result = run(modifiedInstructions)
            if (!result.aborted) return result.accumulator
        }
    }
    throw IllegalStateException("No result!")
}

fun run(instructions: List<Instruction>): Result {
    var accumulator = 0
    var ip = 0
    val seen = mutableSetOf<Int>()
    while (ip < instructions.size) {
        if (ip in seen) return Result(true, accumulator)
        seen.add(ip)
        val instruction = instructions[ip]
        when (instruction.opcode) {
            Opcode.NOP -> {
            }
            Opcode.ACC -> accumulator += instruction.value
            Opcode.JMP -> {
                ip += instruction.value
                continue
            }
        }
        ip++
    }
    return Result(false, accumulator)
}

class Result(val aborted: Boolean, val accumulator: Int)

data class Instruction(val opcode: Opcode, val value: Int)

enum class Opcode { NOP, ACC, JMP }
