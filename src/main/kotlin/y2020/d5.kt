package y2020

import inputLines

fun main() {
    println(inputLines(2020, 5).map(::scan).maxOrNull())
    println(inputLines(2020, 5).map(::scan).sorted().zipWithNext().first { it.first + 1 != it.second }.first + 1)
}

fun scan(boardingPassNumber: String): Int =
    boardingPassNumber.map {
        when (it) {
            'F' -> '0'
            'L' -> '0'
            'B' -> '1'
            'R' -> '1'
            else -> throw NotImplementedError()
        }
    }
        .joinToString("")
        .toInt(2)
