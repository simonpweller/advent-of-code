package y2021

import inputLines

fun main() {
    val (player1Start, player2Start) = inputLines(2021, 21).map { it.last().toString().toInt() }
    val diracDice = DiracDice(player1Start, player2Start)
    println(diracDice.play())
}


private class DiracDice(player1Position: Int, player2Position: Int) {
    val player1 = Player(player1Position, 0)
    val player2 = Player(player2Position, 0)
    val dice = Dice(1)
    var nextPlayer = player1

    fun play(): Int {
        while (!hasEnded) {
            nextPlayer.play(dice)
            toggleNextPlayer()
        }
        return losingPlayer.points * dice.rollCount
    }

    private val hasEnded: Boolean
        get() = player1.hasWon || player2.hasWon

    private val losingPlayer: Player
        get() = if (player1.hasWon) player2 else player1

    private fun toggleNextPlayer() {
        nextPlayer = if (nextPlayer == player1) player2 else player1
    }
}

private class Player(var position: Int, var points: Int) {
    fun play(dice: Dice) {
        repeat(3) {
            position = ((position + dice.roll()) % 10).let { if (it == 0) 10 else it  }
        }
        points += position
    }

    val hasWon: Boolean
        get() = points >= 1000
}

private class Dice(var next: Int, var rollCount: Int = 0) {
    fun roll(): Int = next.also {
        next = (next + 1).let { if (it > 100) it - 100 else it }
        rollCount++
    }
}
