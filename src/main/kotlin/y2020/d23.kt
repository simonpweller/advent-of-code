package y2020

import inputText

fun main() {
    val input = inputText(2020, 23).map { it.toString().toInt() }
    val cupMap = linkCups(input)
    println(cupMap)
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
