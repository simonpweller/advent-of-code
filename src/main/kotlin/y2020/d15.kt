package y2020

import inputText

fun main() {
    val input = inputText(2020, 15).split(",").map(String::toInt)
    val map = mutableMapOf<Int, List<Int>>()
    var lastNumber = 0
    var spokenNumber: Int
    (1 .. 30000000).forEach { turn ->
        spokenNumber = when {
            input.size >= turn -> input[turn - 1]
            map.getOrDefault(lastNumber, emptyList()).size < 2 -> 0
            else -> map.getValue(lastNumber).takeLast(2).let { it[1] - it[0] }
        }
        map[spokenNumber] = map.getOrDefault(spokenNumber, emptyList()).plus(turn).takeLast(2)
        lastNumber = spokenNumber
        if (turn == 2020) println(spokenNumber)
    }
    println(lastNumber)
}
