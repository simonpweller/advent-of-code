package y2021

import inputLines

fun main() {
    val binaries = inputLines(2021, 3)
    println(part1(binaries))
    println(part2(binaries))
}

private fun part1(binaries: List<String>): Int {
    val indices = binaries.first().indices
    val gammaRate = indices.map { index -> mostCommonBitAt(binaries, index) }.joinToString("").toInt(2)
    val epsilonRate = indices.map { index -> leastCommonBitAt(binaries, index) }.joinToString("").toInt(2)
    return gammaRate * epsilonRate
}

private fun part2(binaries: List<String>): Int {
    val oxygenGeneratorRating = findRating(binaries, ::mostCommonBitAt).toInt(2)
    val co2ScrubberRating = findRating(binaries, ::leastCommonBitAt).toInt(2)
    return oxygenGeneratorRating * co2ScrubberRating
}

private fun mostCommonBitAt(binaries: List<String>, index: Int) = if (binaries.count { it[index] == '1' } >= binaries.size / 2) '1' else '0'
private fun leastCommonBitAt(binaries: List<String>, index: Int) = if (mostCommonBitAt(binaries, index) == '1') '0' else '1'

private fun findRating(binaries: List<String>, getRequiredBitAt: (binaries: List<String>, index: Int) -> Char): String {
    var remainingBinaries = binaries
    binaries.first().indices.forEach { index ->
        remainingBinaries = remainingBinaries.filter { it[index] == getRequiredBitAt(remainingBinaries, index) }
        if (remainingBinaries.size == 1) return remainingBinaries.first()
    }
    throw IllegalArgumentException("Duplicate found")
}
