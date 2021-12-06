package y2021

import inputText

fun main() {
    val fish = inputText(2021, 6).split(",").map(String::toInt)
    println(solve(fish, 80))
    println(solve(fish, 256))
}

private fun solve(fish: List<Int>, days: Int): Long {
    var timerCounts = fish.groupBy { it }.mapValues { it.value.count().toLong() }
    repeat(days) {
        timerCounts = (0..7)
            .associateWith { timerCounts.getOrDefault(it + 1, 0) }
            .plus(6 to timerCounts.getOrDefault(7, 0) + timerCounts.getOrDefault(0, 0))
            .plus(8 to timerCounts.getOrDefault(0, 0))
    }
    return timerCounts.values.sum()
}

