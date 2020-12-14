package y2X2X

import inputLines

fun main() {
    val input = inputLines(2020, 14)
    var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    val part1Memory = mutableMapOf<Long, Long>()
    val part2Memory = mutableMapOf<Long, Long>()
    input.forEach { line ->
        if (line.startsWith("mask")) {
            mask = line.substringAfter("mask = ")
        } else {
            val target = line.substringAfter("[").substringBefore("]").toLong()
            val value = line.substringAfter(" = ").toLong()

            part1Memory[target] = applyMask(value, mask)

            part2Targets(target, mask).forEach {
                part2Memory[it] = value
            }
        }
    }
    println(part1Memory.values.sum())
    println(part2Memory.values.sum())
}

fun applyMask(num: Long, mask: String): Long {
    var bits = num.toString(2).padStart(36, '0')
    mask.forEachIndexed { index, bit ->
        when (bit) {
            '1', '0' -> bits = bits.toCharArray().also { it[index] = bit }.joinToString("")
            else -> {
            }
        }
    }
    return bits.toLong(2)
}

fun part2Targets(num: Long, mask: String): List<Long> {
    var bits = num.toString(2).padStart(36, '0')
    mask.forEachIndexed { index, bit ->
        when (bit) {
            '1', 'X' -> bits = bits.toCharArray().also { it[index] = bit }.joinToString("")
            else -> {
            }
        }
    }
    var bitList = listOf(bits)
    var floatingCount = mask.count { it == 'X' }
    while (floatingCount > 0) {
        bitList = bitList.flatMap {
            listOf(
                it.replaceFirst('X', '1'),
                it.replaceFirst('X', '0')
            )
        }
        floatingCount--
    }
    return bitList.map { it.toLong(2) }
}
