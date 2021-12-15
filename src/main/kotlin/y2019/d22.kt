package y2019

import inputLines

fun main() {
    val input = inputLines(2019, 22)
    val deck = input.fold((0 until 10007L).toList()) { acc, line ->
        when {
            line.startsWith("cut") -> cut(acc, line.split(" ").last().toInt())
            line.startsWith("deal with increment") -> dealWithIncrement(acc, line.split(" ").last().toInt())
            else -> dealIntoNewStack(acc)
        }
    }
    println(deck.indexOfFirst { it == 2019L })
}

private fun cut(deck: List<Long>, count: Int) =
    if (count < 0) deck.takeLast(-count) + deck.dropLast(-count) else deck.drop(count) + deck.take(count)

private fun dealIntoNewStack(deck: List<Long>) = deck.reversed()

private fun dealWithIncrement(deck: List<Long>, increment: Int): List<Long> {
    val newDeck = LongArray(deck.size)
    var index = 0
    deck.forEach { card ->
        newDeck[index] = card
        index = (index + increment) % deck.size
    }
    return newDeck.toList()
}
