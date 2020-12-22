package y2020

import inputChunks

fun main() {
    val (player1, player2) = inputChunks(2020, 22).map { chunk ->
        val id = chunk.lines().first()[7].toInt()
        val deck = chunk.lines().drop(1).map(String::toInt)
        Player(id, deck)
    }

    println(part1(player1, player2))
    println(part2(player1, player2))
}

private fun part1(player1: Player, player2: Player): Int {
    while (player1.deck.isNotEmpty() && player2.deck.isNotEmpty()) {
        val player1Card = player1.deck.first()
        val player2Card = player2.deck.first()
        if (player1Card > player2Card) {
            player1.deck = player1.deck.drop(1).plus(player1Card).plus(player2Card)
            player2.deck = player2.deck.drop(1)
        } else {
            player1.deck = player1.deck.drop(1)
            player2.deck = player2.deck.drop(1).plus(player2Card).plus(player1Card)
        }
    }

    return player1.deck.plus(player2.deck).reversed().mapIndexed { index, card -> card * (index + 1) }.sum()
}

private fun part2(player1: Player, player2: Player): Int {
    return 0
}

data class Player(val id: Int, var deck: List<Int>)
