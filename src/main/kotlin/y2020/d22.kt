package y2020

import inputChunks
import y2020.Player.*

fun main() {
    val (deck1, deck2) = inputChunks(2020, 22).map { chunk ->
        chunk.lines().drop(1).map(String::toInt)
    }

    println(part1(deck1, deck2))
    println(playGame(deck1, deck2).score)
}

private fun part1(startingDeck1: List<Int>, staringDeck2: List<Int>): Int {
    var deck1 = startingDeck1
    var deck2 = staringDeck2
    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val card1 = deck1.first()
        val card2 = deck2.first()
        if (card1 > card2) {
            deck1 = deck1.drop(1).plus(card1).plus(card2)
            deck2 = deck2.drop(1)
        } else {
            deck1 = deck1.drop(1)
            deck2 = deck2.drop(1).plus(card2).plus(card1)
        }
    }

    return if (deck1.isNotEmpty()) scoreDeck(deck1) else scoreDeck(deck2)
}

private fun playGame(startingDeck1: List<Int>, staringDeck2: List<Int>): GameResult {
    var deck1 = startingDeck1
    var deck2 = staringDeck2

    val seen = mutableSetOf<Pair<List<Int>, List<Int>>>()

    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        if (Pair(deck1, deck2) in seen) {
            return GameResult(PLAYER_1, scoreDeck(deck1))
        } else {
            seen.add(Pair(deck1, deck2))
            val card1 = deck1.first()
            val card2 = deck2.first()

            val roundWinner: Player = when {
                deck1.size > card1 && deck2.size > card2 -> {
                    playGame(deck1.drop(1).take(card1), deck2.drop(1).take(card2)).winner // recursive case
                }
                else -> {
                    if (card1 > card2) PLAYER_1 else PLAYER_2 // base case
                }
            }

            if (roundWinner == PLAYER_1) {
                deck1 = deck1.drop(1).plus(card1).plus(card2)
                deck2 = deck2.drop(1)
            } else {
                deck1 = deck1.drop(1)
                deck2 = deck2.drop(1).plus(card2).plus(card1)
            }
        }
    }
    return if (deck1.isNotEmpty()) GameResult(PLAYER_1,scoreDeck(deck1)) else GameResult(PLAYER_2, scoreDeck(deck2))
}

private fun scoreDeck(deck: List<Int>): Int = deck.reversed().mapIndexed { index, card -> card * (index + 1) }.sum()

private data class GameResult(val winner: Player, val score: Int)
private enum class Player { PLAYER_1, PLAYER_2 }
