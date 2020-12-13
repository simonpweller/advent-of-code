package y2020

import inputLines

fun main() {
    val input = inputLines(2020, 13)
    val startingTimestamp = input.first().toInt()

    val busses = input[1].split(",").mapIndexedNotNull { index, busId ->
        if (busId == "x") return@mapIndexedNotNull null
        Bus(busId.toInt(), index)
    }

    println(part1(busses, startingTimestamp))
    println(part2(busses))
}

private fun part1(busses: List<Bus>, startingTimestamp: Int): Int {
    val waitTimes = busses.map { bus -> WaitTime(bus.id, bus.id - (startingTimestamp % bus.id)) }
    return waitTimes.minByOrNull { it.waitTime }!!.let { it.busId * it.waitTime }
}

private fun part2(busses: List<Bus>): Long {
    return (0..busses.lastIndex).fold(1L) { timestamp, index ->
        val stepsize = busses.subList(0, index).fold(1L) { product, bus -> product * bus.id }
        generateSequence(timestamp) { it + stepsize }.first {
            if (index == 0) {
                it % busses[index].id == 0L
            } else {
                it % busses[index].id == (busses[index].id - (busses[index].index % busses[index].id)).toLong()
            }
        }
    }
}

private data class Bus(val id: Int, val index: Int)
private data class WaitTime(val busId: Int, val waitTime: Int)
