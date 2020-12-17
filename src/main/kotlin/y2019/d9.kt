package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 9).split(",").map(String::toLong)
    println(IntcodeComputer(intcode, listOf(1)).run())
    println(IntcodeComputer(intcode, listOf(2)).run())
}
