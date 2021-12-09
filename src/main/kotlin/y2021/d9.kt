package y2021

import inputLines
import y2017.Point
import java.util.*

fun main() {
    val cave = inputLines(2021, 9).map { line -> line.toList().map(Char::toString).map(String::toInt) }
    println(part1(cave))
    println(part2(cave))
}

private fun part1(heightMap: List<List<Int>>): Int {
    val cave = Cave(heightMap)
    val lows = cave.getLows()
    val riskFactors = lows.map { cave.heightAt(it) + 1 }
    return riskFactors.sum()
}

private fun part2(heightMap: List<List<Int>>): Int {
    val cave = Cave(heightMap)
    val lows = cave.getLows()
    val basinSizes = lows.map { cave.getBasinSize(it) }
    return basinSizes.sortedDescending().take(3).reduce { a, b -> a * b }
}

private class Cave(private val map: List<List<Int>>) {
    val points = map.indices.flatMap { y -> map.first().indices.map { x -> Point(x, y) } }

    fun heightAt(point: Point) = map[point.y][point.x]

    fun isLow(point: Point) = getNeighbours(point).all { neighbour -> heightAt(neighbour) > heightAt(point) }

    fun getLows() = points.filter(this::isLow)

    fun getNeighbours(point: Point): List<Point> {
        val (x, y) = point
        val left = if (x == 0) null else Point(x - 1, y)
        val top = if (y == 0) null else Point(x, y - 1)
        val right = if (x == map.first().lastIndex) null else Point(x + 1, y)
        val bottom = if (y == map.lastIndex) null else Point(x, y + 1)
        return listOfNotNull(left, top, right, bottom)
    }

    fun getBasinSize(point: Point): Int {
        val accountedFor = mutableSetOf(point)
        val queue = LinkedList<Point>().also { it.add(point) }
        var basinSize = 0
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (heightAt(next) != 9) {
                basinSize++
                getNeighbours(next).let { neighbours ->
                    queue.addAll(neighbours.filter { !accountedFor.contains(it) })
                    accountedFor.addAll(neighbours)
                }
            }
        }
        return basinSize
    }
}
