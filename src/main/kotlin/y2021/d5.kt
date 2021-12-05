package y2021

import inputLines

fun main() {
    val lines = inputLines(2021, 5)
        .map { line -> line.split(" -> ").map { it.split(",").map(String::toInt) }.map { (x, y) -> Point(x, y) } }
        .map { (start, end) -> Line(start, end, getLineType(start, end)) }
    println(part1(lines))
    println(part2(lines))
}

private fun part1(lines: List<Line>): Int {
    val grid = Array(1000) { IntArray(1000) { 0 } }

    lines
        .filter { it.type != Type.DIAGONAL }
        .forEach { it.points.forEach { point -> grid[point.y][point.x]++ } }

    return grid.sumOf { line -> line.count { it > 1 } }
}

private fun part2(lines: List<Line>): Int {
    val grid = Array(1000) { IntArray(1000) { 0 } }

    lines.forEach { it.points.forEach { point -> grid[point.y][point.x]++ } }

    return grid.sumOf { line -> line.count { it > 1 } }
}

private data class Line(val start: Point, val end: Point, val type: Type) {
    val points: List<Point>
        get() = when (type) {
            Type.HORIZONTAL -> xRange.map { x -> Point(x, start.y) }
            Type.VERTICAL -> yRange.map { y -> Point(start.x, y) }
            Type.DIAGONAL -> xRange.zip(yRange).map { Point(it.first, it.second) }
        }
    val xRange: IntProgression
        get() = if (end.x > start.x) (start.x..end.x) else (start.x downTo end.x)
    val yRange: IntProgression
        get() = if (end.y > start.y) (start.y..end.y) else (start.y downTo end.y)
}

private fun getLineType(start: Point, end: Point) = when {
    start.x == end.x -> Type.VERTICAL
    start.y == end.y -> Type.HORIZONTAL
    else -> Type.DIAGONAL
}

private enum class Type {
    HORIZONTAL,
    VERTICAL,
    DIAGONAL,
}

private data class Point(val x: Int, val y: Int)
