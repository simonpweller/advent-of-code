package y2020

import charCounts
import inputTextSplitByBlankLines

fun main() {
    val groups = inputTextSplitByBlankLines(2020, 6)
    println(groups.map { it.lines().joinToString("") }.map { charCounts(it).keys.size }.sum())
    println(groups.map { group ->
        val members = group.lines()
        charCounts(members.joinToString("")).values.filter { it == members.size }.size
    }.sum())
}
