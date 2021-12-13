package y2021

import inputLines

fun main() {
    val input = inputLines(2021, 13)
    val dots =
        input.takeWhile { it.isNotBlank() }.map { it.split(",") }.map { Pair(it.first().toInt(), it.last().toInt()) }
            .toSet()
    val folds = input.drop(dots.size + 1).map { Fold(Axis.of(it), it.substringAfter("=").toInt()) }
    println(part1(dots, folds))
    println(part2(dots, folds))
}

private data class Fold(val axis: Axis, val position: Int)
private enum class Axis {
    X, Y;

    companion object {
        fun of(input: String): Axis = if (input[11] == 'x') X else Y
    }
}

private fun part1(dots: Set<Pair<Int, Int>>, folds: List<Fold>): Int = applyFold(dots, folds.first()).size
private fun part2(dots: Set<Pair<Int, Int>>, folds: List<Fold>): String {
    val foldedDots = folds.fold(dots) { d, fold -> applyFold(d, fold) }

    val xRange = 0..foldedDots.maxOf { it.first }
    val yRange = 0..foldedDots.maxOf { it.second }

    return yRange.joinToString(System.lineSeparator()) { y ->
        xRange.map { x -> if (foldedDots.contains(Pair(x, y))) '#' else '.' }.joinToString("")
    }
}

private fun applyFold(dots: Set<Pair<Int, Int>>, fold: Fold): Set<Pair<Int, Int>> = when (fold.axis) {
    Axis.X -> dots.map { (x, y) ->
        if (x > fold.position) Pair(2 * fold.position - x, y) else Pair(x, y)
    }
    Axis.Y -> dots.map { (x, y) ->
        if (y > fold.position) Pair(x, 2 * fold.position - y) else Pair(x, y)
    }
}.toSet()


