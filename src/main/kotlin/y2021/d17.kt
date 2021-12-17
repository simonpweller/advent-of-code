package y2021

import inputText
import kotlin.math.max

fun main() {
    val input = inputText(2021, 17)
    val targetXRange = input.substringAfter("x=").substringBefore(",").let(::rangeStringToRange)
    val targetYRange = input.substringAfter("y=").let(::rangeStringToRange)

    val xVelocityRange = (0..targetXRange.last)
    val yVelocityRange = (targetYRange.first..-targetYRange.first)

    val paths = (xVelocityRange).flatMap { x -> yVelocityRange.map { y -> getPath(x,  y, targetXRange, targetYRange) } }
    val hittingPaths = paths.filter(Path::hits)
    println(hittingPaths.maxOf { it.maxY })
    println(hittingPaths.size)
}

private fun rangeStringToRange(rangeString: String): IntRange =
    rangeString.split("..").let { (it.first().toInt()..it.last().toInt()) }

private fun getPath(initialXVelocity: Int, initialYVelocity: Int, targetXRange: IntRange, targetYRange: IntRange): Path =
    generateSequence(Path(0, 0, 0, initialXVelocity, initialYVelocity, false)) { (x, y, maxY, xVelocity, yVelocity, hits) ->
        val nextX = x + xVelocity
        val nextY = y + yVelocity
        val nextHits = hits || (nextX in targetXRange && nextY in targetYRange)
        Path(nextX, nextY, max(maxY, nextY), adjustXVelocity(xVelocity), yVelocity - 1, nextHits, )
    }.takeWhile { it.x <= targetXRange.last && it.y >= targetYRange.first }.last()

private fun adjustXVelocity(xVelocity: Int) = when {
    xVelocity > 0 -> xVelocity - 1
    xVelocity < 0 -> xVelocity + 1
    else -> 0
}

private data class Path(val x: Int, val y: Int, val maxY: Int, val xVelocity: Int, val yVelocity: Int, val hits: Boolean)
