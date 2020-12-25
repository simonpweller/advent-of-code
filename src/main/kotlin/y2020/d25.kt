package y2020

import inputLines

fun main() {
    val (cardPublicKey, doorPublicKey) = inputLines(2020, 25).map(String::toLong)

    val cardLoopSize = findLoopSize(cardPublicKey, 7)
    val doorLoopSize = findLoopSize(doorPublicKey, 7)

    println(transform(cardPublicKey, doorLoopSize))
    println(transform(doorPublicKey, cardLoopSize))
}

fun transform(subjectNumber: Long, loopSize: Int): Long {
    var value = 1L
    repeat(loopSize) {
        value *= subjectNumber
        value %= 20201227
    }
    return value
}

fun findLoopSize(targetNumber: Long, subjectNumber: Long): Int {
    var value = 1L
    var loopSize = 0
    while (value != targetNumber) {
        loopSize++
        value *= subjectNumber
        value %= 20201227
    }
    return loopSize
}
