package y2020

import inputText

fun main() {
    val input = inputText(2020, 23)
    println(part1(input))
}

private fun part1(input: String) =
    CupArrangement(input.map { it.toString().toInt() }, 0, 9).apply { move(100) }.cupOrder.joinToString("")

data class CupArrangement(var cups: List<Int>, var currentIndex: Int = 0, val maxLabel: Int) {
    private fun move() {
        val currentCup = cups[currentIndex]
        val cupsToMove =
            cups.drop(currentIndex + 1).take(3).let { it.plus(cups.take(3 - it.size)) }
        var destinationCupLabel = cups[currentIndex] - 1
        if (destinationCupLabel < 1) destinationCupLabel = maxLabel
        while (destinationCupLabel in cupsToMove) {
            destinationCupLabel--
            if (destinationCupLabel < 1) destinationCupLabel = maxLabel
        }
        val remainingCups = cups.minus(cupsToMove)
        val destinationCupIndex = remainingCups.indexOf(destinationCupLabel)
        cups = remainingCups.subList(0, destinationCupIndex + 1) + cupsToMove +
                remainingCups.subList(destinationCupIndex + 1, remainingCups.size)
        currentIndex = if (cups.indexOf(currentCup) == cups.lastIndex) 0 else cups.indexOf(currentCup) + 1
    }

    fun move(times: Int) {
        repeat(times) { this.move() }
    }

    val cupOrder: List<Int>
        get() = cups.subList(cups.indexOf(1) + 1, cups.lastIndex) + cups.subList(0, cups.indexOf(1))
}
