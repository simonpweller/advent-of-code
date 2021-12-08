package y2021

import inputLines

fun main() {
    val inputs = inputLines(2021, 8)
    val outputs = inputs.map(::decodeOutput)
    println(part1(outputs))
    println(part2(outputs))
}

private fun part1(inputs: List<Int>): Int = inputs.sumOf { it.toString().count { digit -> listOf('1', '4', '7', '8').contains(digit) } }
private fun part2(inputs: List<Int>): Int = inputs.sum()

private fun decodeOutput(input: String): Int {
    val (digits, displayDigits) = input.split("|")
        .map { digitString -> digitString.trim().split(" ").map(String::toSet) }
    val digitMap = identifyDigits(digits)
    return displayDigits.joinToString("") { digit -> digitMap.getValue(digit) }.toInt()
}

private fun identifyDigits(digits: List<Set<Char>>): Map<Set<Char>, String> {
    val one = digits.first { it.size == 2 }
    val seven = digits.first { it.size == 3 }
    val four = digits.first { it.size == 4 }
    val eight = digits.first { it.size == 7 }
    val fiveSegmentDigits = digits.filter { it.size == 5 }
    val sixSegmentDigits = digits.filter { it.size == 6 }

    val cSegment = one.first { !sixSegmentDigits.all { sixSegmentDigit -> sixSegmentDigit.contains(it) } }
    val fSegment = one.first { it != cSegment }
    val six = sixSegmentDigits.first { !it.contains(cSegment) }
    val three = fiveSegmentDigits.first { it.containsAll(one) }
    val two = fiveSegmentDigits.first { it.contains(cSegment) && !it.contains(fSegment) }
    val five = fiveSegmentDigits.first { !it.contains(cSegment) && it.contains(fSegment) }
    val nine = sixSegmentDigits.first { it.containsAll(three) }
    val zero = sixSegmentDigits.first { it != nine && it != six }

    return mapOf(
        zero to "0",
        one to "1",
        two to "2",
        three to "3",
        four to "4",
        five to "5",
        six to "6",
        seven to "7",
        eight to "8",
        nine to "9"
    )
}
