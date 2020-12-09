package y2020

import inputLines
import subListsOfSize
import java.lang.IllegalArgumentException

fun main() {
    val nums = inputLines(2020, 9).map(String::toLong)
    val part1 = part1(nums)
    println(part2(nums, part1))
}

fun part2(nums: List<Long>, part1: Long): Any {
    nums.indices.forEach { i ->
        var rangeIdx = i
        var rangeSum = nums[rangeIdx]
        while(rangeSum < part1 && rangeIdx < nums.lastIndex) {
            rangeIdx++
            rangeSum += nums[rangeIdx]
        }
        if (rangeSum == part1 && rangeIdx != i) {
            val range = nums.subList(i, rangeIdx + 1)
            return range.minOrNull()!! + range.maxOrNull()!!
        }
    }
    throw IllegalArgumentException("No solution")
}

fun part1(nums: List<Long>): Long {
    nums.indices.forEach { i ->
        if (i < 25) return@forEach
        val previous25 = nums.subList(i - 25, i)
        if (!subListsOfSize(previous25, 2).map(List<Long>::sum).contains(nums[i])) {
            return nums[i]
        }
    }
    throw IllegalArgumentException("No solution")
}
