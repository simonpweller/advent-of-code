package y2020

import inputText

fun main() {
    val input = inputText(2020, 23).map { it.toString().toInt() }
    println(part1(input))

    val cup1 = play(input.plus(10 .. 1_000_000), 10_000_000).getValue(1)
    println(cup1.next.label.toLong() * cup1.next.next.label)
}

private fun part1(input: List<Int>): String {
    val cupMap = play(input, 100)
    var current = cupMap.getValue(1)
    val values = mutableListOf<Int>()
    do {
        values.add(current.next.label)
        current = current.next
    } while (current.label != 1)
    return values.joinToString("")
}

private fun play(input: List<Int>, rounds: Int): Map<Int, Cup> {
    val cupMap = linkCups(input)
    var currentCup = cupMap.getValue(input.first())
    val maxLabel = cupMap.keys.maxOrNull() ?: throw Exception()

    repeat(rounds) {
        val cup1 = currentCup.next
        val cup2 = cup1.next
        val cup3 = cup2.next
        currentCup.next = cup3.next

        var destinationCupLabel = currentCup.label - 1
        if (destinationCupLabel < 1) destinationCupLabel = maxLabel
        while (destinationCupLabel in listOf(cup1.label, cup2.label, cup3.label)) {
            destinationCupLabel--
            if (destinationCupLabel < 1) destinationCupLabel = maxLabel
        }

        val destinationCup = cupMap.getValue(destinationCupLabel)
        val destinationCupNext = destinationCup.next
        destinationCup.next = cup1
        cup3.next = destinationCupNext

        currentCup = currentCup.next
    }

    return cupMap
}

private fun linkCups(input: List<Int>): Map<Int, Cup> {
    val first = Cup(input.first())
    val cupMap = mutableMapOf(first.label to first)
    var prev = first
    input.drop(1).forEach {
        val cup = Cup(it)
        cupMap[it] = cup
        prev.next = cup
        prev = cup
    }
    prev.next = first
    return cupMap
}

class Cup(val label: Int) {
    private var _next: Cup? = null

    var next: Cup
        get() = _next!!
        set(value) { _next = value}

    override fun toString(): String = "$label: ${next.label}"
}
