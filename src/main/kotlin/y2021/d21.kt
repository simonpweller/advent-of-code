package y2021

import inputLines

fun main() {
    val (player1Start, player2Start) = inputLines(2021, 21).map { it.last().toString().toInt() }
    val diracDice = DiracDice(player1Start, player2Start)
    println(diracDice.play())
}


private class DiracDice(var player1Position: Int, var player2Position: Int) {
    var player1Points = 0
    var player2Points = 0
    var nextDie = 1
    var diceRolls = 0
    var player1Next = true

    fun play(): Int {
        while (player1Points < 1000 && player2Points < 1000) {
            if (player1Next) {
                repeat(3) {
                    player1Position = ((player1Position + nextDie) % 10).let { if (it == 0) 10 else it  }
                    nextDie = (nextDie + 1).let { if (it > 100) it - 100 else it }
                    diceRolls++
                }
                player1Points += player1Position
            } else {
                repeat(3) {
                    player2Position = ((player2Position + nextDie) % 10).let { if (it == 0) 10 else it  }
                    nextDie = (nextDie + 1).let { if (it > 100) it - 100 else it }
                    diceRolls++
                }
                player2Points += player2Position
            }
            player1Next = !player1Next
        }
        return if (player1Points >= 1000) player2Points * diceRolls else player1Points * diceRolls
    }
}
