package y2021

import inputLines
import subListsOfSize

fun main() {
    val lines = inputLines(2021, 18)
    println(part1(lines))
    println(part2(lines))
}

private fun part1(lines: List<String>): Int {
    var res = parse(lines.first())
    lines.drop(1).forEach { line ->
        res = add(res, parse(line))
        res = reduce(res)
    }
    return getMagnitude(res)
}

private fun part2(lines: List<String>): Int {
    val sublists = subListsOfSize(lines.map(::parse), 2)
    val part2 = sublists.maxOf {
        getMagnitude(reduce(add(it.first(), it.last())))
    }
    return part2
}

private fun parse(string: String): SnailfishNumber = string.map { it.toString() }
private typealias SnailfishNumber = List<String>
private fun add(a: SnailfishNumber, b: SnailfishNumber) = listOf("[") + a + "," + b + "]"
private fun reduce(a: SnailfishNumber): SnailfishNumber {
    var prev = a
    while (true) {
        var next = explode(prev)
        if (next == prev) next = split(prev)
        if (next == prev) return next else prev = next
    }
}

private fun explode(a: SnailfishNumber): SnailfishNumber {
    var depth = 0
    a.forEachIndexed { index, s ->
        if (s == "[") depth++
        if (s == "]") depth--
        if (depth == 5) {
            val explodingPair = a.subList(index, index + 5)
            val leftNum = explodingPair[1].toInt()
            val rightNum = explodingPair[3].toInt()
            val leftPad = a.subList(0, index).toMutableList()
            val leftReplaceIndex = leftPad.indexOfLast { !setOf("[", "]", ",").contains(it) }
            if (leftReplaceIndex != -1) leftPad[leftReplaceIndex] = (leftPad[leftReplaceIndex].toInt() + leftNum).toString()
            val rightPad = a.subList(index + 5, a.size).toMutableList()
            val rightReplaceIndex = rightPad.indexOfFirst { !setOf("[", "]", ",").contains(it) }
            if (rightReplaceIndex != -1) rightPad[rightReplaceIndex] = (rightPad[rightReplaceIndex].toInt() + rightNum).toString()
            return leftPad + "0" + rightPad
        }
    }
    return a
}
private fun split(a: SnailfishNumber): SnailfishNumber {
    val splittableNumIndex = a.indexOfFirst { it.length > 1 }
    if (splittableNumIndex == -1) return a
    val splittableNum = a[splittableNumIndex].toInt()
    val leftNum = splittableNum / 2
    val rightNum = splittableNum / 2 + splittableNum % 2
    return a.subList(0, splittableNumIndex) + "[" + leftNum.toString() + "," + rightNum.toString() + "]" + a.subList(splittableNumIndex + 1, a.size)
}

private fun getMagnitude(a: SnailfishNumber): Int {
    if (a.size == 1) return a.first().toInt()
    if (a.size == 5) {
        return a[1].toInt() * 3 + a[3].toInt() * 2
    }
    var depth = 0
    a.forEachIndexed { index, s ->
        if (s == "[") depth++
        if (s == "]") depth--
        if (s == "," && depth == 1) {
            return getMagnitude(a.subList(1, index)) * 3 + getMagnitude(a.subList(index + 1, a.lastIndex)) * 2
        }
    }
    throw IllegalArgumentException("Expression is not balanced")
}
