package y2021

import inputText
import kotlin.math.max

fun main() {
    val input = inputText(2021, 17)
    val targetXRange = input.substringAfter("x=").substringBefore(",").let(::rangeStringToRange)
    val targetYRange = input.substringAfter("y=").let(::rangeStringToRange)

    val xVelocityOptions = (0..targetXRange.last)
    val yVelocityOptions = (targetYRange.first..-targetYRange.first)

    val paths = (xVelocityOptions).flatMap { x -> yVelocityOptions.map { y -> getPath(x,  y, targetXRange, targetYRange) } }
    println(
        paths
            .filter { it.hits }
            .maxOf { it.maxY }
    )

    println(
        paths
            .filter { it.hits }
            .size
    )
}

private fun rangeStringToRange(rangeString: String): IntRange =
    rangeString.split("..").let { (it.first().toInt()..it.last().toInt()) }

private fun getPath(initialXVelocity: Int, initialYVelocity: Int, targetXRange: IntRange, targetYRange: IntRange): Path {
    var xVelocity = initialXVelocity
    var yVelocity = initialYVelocity
    var x = 0
    var y = 0
    var maxY = y
    var hits = false
    do {
        x += xVelocity
        y += yVelocity
        maxY = max(maxY, y)
        xVelocity = adjustXVelocity(xVelocity)
        yVelocity--
        if (x in targetXRange && y in targetYRange) hits = true
    } while (x <= targetXRange.last && y >= targetYRange.first)
    return Path(maxY, initialXVelocity, initialYVelocity, hits)
}

private fun adjustXVelocity(xVelocity: Int) = when {
    xVelocity > 0 -> xVelocity - 1
    xVelocity < 0 -> xVelocity + 1
    else -> 0
}

private data class Path(val maxY: Int, val initialXVelocity: Int, val initialYVelocity: Int, val hits: Boolean)
