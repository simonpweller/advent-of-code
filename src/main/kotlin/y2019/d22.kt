package y2019

import inputLines

fun main() {
    val input = inputLines(2019, 22)
    val deckSize = 10007L
    val res = input.fold(2019L) { acc, line ->
        when {
            line.startsWith("cut") -> cut(acc, line.split(" ").last().toInt(), deckSize)
            line.startsWith("deal with increment") -> dealWithIncrement(acc, line.split(" ").last().toInt(), deckSize)
            else -> dealIntoNewStack(acc, deckSize)
        }
    }
    println(res)
}

private fun cut(position: Long, count: Int, deckSize: Long) =
    if (count < 0) {
        if (position >= deckSize + count) position - count - deckSize else position - count
    } else {
        if (position >= count) position - count else deckSize - count + position
    }
private fun dealIntoNewStack(position: Long, deckSize: Long) = deckSize - 1 - position
private fun dealWithIncrement(position: Long, increment: Int, deckSize: Long): Long {
    var acc = 0L
    var gap = -1L
    var positionsOnNextLine = (deckSize + gap) / increment
    while (acc + positionsOnNextLine < position) {
        acc += positionsOnNextLine
        gap = (deckSize + gap) % increment
        positionsOnNextLine = (deckSize + gap) / increment
    }
    val stepsToTake = position - acc
    return stepsToTake * increment - gap - 1
}
