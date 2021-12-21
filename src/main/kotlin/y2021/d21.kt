package y2021

import inputLines
import java.util.*
import kotlin.math.max

fun main() {
    val (player1Start, player2Start) = inputLines(2021, 21).map { it.last().toString().toInt() }
    val diracDice = DiracDice(player1Start, player2Start)
    println(diracDice.play())
    println(part2(player1Start, player2Start))
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
            position = ((position + dice.roll()) % 10).let { if (it == 0) 10 else it }
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

private fun part2(player1Position: Int, player2Position: Int): Long {
    val initialGameState = GameState(player1Position, player2Position, true, 0, 0)
    val gameStateTransitionMap = constructGameStateTransitionMap(initialGameState)
    val gameStateWinCountsMap = constructGameStateWinCountMap(gameStateTransitionMap)
    val finalWinCounts = gameStateWinCountsMap.getValue(initialGameState)
    return max(finalWinCounts.player1, finalWinCounts.player2)
}

private fun constructGameStateTransitionMap(initialGameState: GameState): Map<GameState, List<GameStateCount>> {
    val gameStateTransitionMap = mutableMapOf<GameState, List<GameStateCount>>()
    val queue = LinkedList<GameState>()
    queue.add(initialGameState)

    while (queue.isNotEmpty()) {
        val gameState = queue.poll()
        val split = gameState.split()
        gameStateTransitionMap[gameState] = split
        if (!gameState.hasWinner) {
            queue.addAll(split.map { it.gameState }
                .filterNot { gameStateTransitionMap.containsKey(it) })
        }
    }
    return gameStateTransitionMap
}

private fun constructGameStateWinCountMap(gameStateTransitionMap: Map<GameState, List<GameStateCount>>): Map<GameState, WinCounts> {
    val resolvedGames = gameStateTransitionMap.keys.filter(GameState::hasWinner).associateWith {
        WinCounts(
            if (it.player1Points > it.player2Points) 1 else 0,
            if (it.player2Points > it.player1Points) 1 else 0
        )
    }.toMutableMap()

    val queue = LinkedList(gameStateTransitionMap.keys - resolvedGames.keys)

    while (queue.isNotEmpty()) {
        val gameState = queue.poll()
        val children = gameState.split()
        if (children.all { resolvedGames.contains(it.gameState) }) {
            resolvedGames[gameState] = children
                .map { (childState, count) -> resolvedGames.getValue(childState) * count }
                .reduce { a, b -> WinCounts(a.player1 + b.player1, a.player2 + b.player2) }
        } else {
            queue.add(gameState)
        }
    }

    return resolvedGames
}

private data class WinCounts(var player1: Long, var player2: Long) {
    operator fun times(multiplier: Long) = WinCounts(player1 * multiplier, player2 * multiplier)
}

private data class GameStateCount(val gameState: GameState, val count: Long)

private data class GameState(
    val player1Position: Int,
    val player2Position: Int,
    val player1Next: Boolean,
    val player1Points: Int,
    val player2Points: Int,
) {
    val hasWinner = player1Points >= 21 || player2Points >= 21

    fun getNext(diceTotal: Int): GameState {
        val nextPosition = (if (player1Next) player1Position else player2Position).let { position ->
            ((position + diceTotal) % 10).let { if (it == 0) 10 else it }
        }
        return if (player1Next) {
            GameState(nextPosition, player2Position, false, player1Points + nextPosition, player2Points)
        } else {
            GameState(player1Position, nextPosition, true, player1Points, player2Points + nextPosition)
        }
    }

    fun split(): List<GameStateCount> =
        mapOf(
            3 to 1L,
            4 to 3L,
            5 to 6L,
            6 to 7L,
            7 to 6L,
            8 to 3L,
            9 to 1L,
        ).map { (diceTotal, count) -> GameStateCount(getNext(diceTotal), count) }
}

