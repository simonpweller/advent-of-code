package y2021

import inputText

fun main() {
    val input = inputText(2021, 17)
    val targetXRange = input.substringAfter("x=").substringBefore(",").let(::rangeStringToRange)
    val targetYRange = input.substringAfter("y=").let(::rangeStringToRange)

    val xVelocityOptions = (0..targetXRange.last / 3)
    val yVelocityOptions = (0..-targetYRange.first)

    val paths = (xVelocityOptions).flatMap { x -> yVelocityOptions.map { y -> path(x,  y, targetXRange, targetYRange) } }
    println(paths.size)
    println(
        paths
            .filter { it.steps.last().first in targetXRange && it.steps.last().second in targetYRange }
            .maxOf { it.steps.maxOf { it.second } }
    )
}

private fun rangeStringToRange(rangeString: String): IntRange =
    rangeString.split("..").let { (it.first().toInt()..it.last().toInt()) }

private fun path(xVelocity: Int, yVelocity: Int, targetXRange: IntRange, targetYRange: IntRange): Path {
    return generateSequence(
        Path(
            listOf(Pair(0, 0)),
            xVelocity,
            yVelocity
        )
    ) {
        Path(
            it.steps + Pair(it.steps.last().first + it.xVelocity, it.steps.last().second + it.yVelocity),
            adjustXVelocity(it.xVelocity),
            it.yVelocity - 1,
        )
    }.takeWhile { it.steps.last().first <= targetXRange.last && it.steps.last().second >= targetYRange.last }.last()
}

private fun adjustXVelocity(xVelocity: Int) = when {
    xVelocity > 0 -> xVelocity - 1
    xVelocity < 0 -> xVelocity + 1
    else -> 0
}

private data class Path(val steps: List<Pair<Int, Int>>, val xVelocity: Int, val yVelocity: Int)
