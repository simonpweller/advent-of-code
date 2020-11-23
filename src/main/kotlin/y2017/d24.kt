package y2017

import inputLines
import java.util.*

fun main() {
    val bricks = parseInput(inputLines(2017, 24))
    println(
        validBridges(
            bricks
        ).maxByOrNull(Bridge::length)?.length()
    )
    println(
        validBridges(
            bricks
        ).sortedWith(compareBy({it.size}, {it.length()})).last().length()
    )
}

data class Brick(val left: Int, val right: Int) {
    fun connectsTo(num: Int) = left == num || right == num
}

data class Bridge(val bricks: List<Brick> = emptyList()) {
    val size: Int = bricks.size

    operator fun plus(brick: Brick) = Bridge(bricks + brick)
    fun length() = bricks.map { it.left + it.right }.sum()
}

data class BridgeBuilder(val bridge: Bridge, val remainingBricks: Set<Brick>, val end: Int = 0)

fun parseInput(input: List<String>): Set<Brick> = input.map {
    val (left, right) = it.split("/").map(String::toInt)
    Brick(left, right)
}.toSet()

fun validBridges(bricks: Set<Brick>): Set<Bridge> {
    val bridges = mutableSetOf<Bridge>()
    val bridgeBuilds = PriorityQueue<BridgeBuilder>(compareBy { it.bridge.bricks.size })
    bridgeBuilds.add((BridgeBuilder(Bridge(), bricks)))

    while (bridgeBuilds.peek() != null) {
        val bridgeBuilder = bridgeBuilds.poll()
        bridges.add(bridgeBuilder.bridge)
        val nextBricks = bridgeBuilder.remainingBricks.filter { it.connectsTo(bridgeBuilder.end) }
        bridgeBuilds.addAll(nextBricks.map {
            bridgeBuilder.copy(
                bridge = bridgeBuilder.bridge + it,
                remainingBricks = bridgeBuilder.remainingBricks - it,
                end = if (it.left == bridgeBuilder.end) it.right else it.left
            )
        })
    }

    return bridges
}
