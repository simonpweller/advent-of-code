package y2020

import inputLines

fun main() {
    val input = inputLines(2020, 13)
    val startingTimestamp = input.first().toInt()

    val busses = input[1].split(",").mapIndexedNotNull { index, busId ->
        if (busId == "x") null else Bus(busId.toLong(), index)
    }

    println(part1(busses, startingTimestamp))
    println(part2(busses))
}

private fun part1(busses: List<Bus>, startingTimestamp: Int): Long {
    val waitTimes = busses.map { bus -> WaitTime(bus.id, bus.id - (startingTimestamp % bus.id)) }
    return waitTimes.minByOrNull { it.waitTime }!!.let { it.busId * it.waitTime }
}

private fun part2(busses: List<Bus>): Long {
    var stepsize = busses.first().id
    var timestamp = busses.first().id
    busses.drop(1).forEach { bus ->
        timestamp = generateSequence(timestamp) { it + stepsize }.first {
            (it + bus.index) % bus.id == 0L
        }
        stepsize *= bus.id
    }
    return timestamp
}

private data class Bus(val id: Long, val index: Int)
private data class WaitTime(val busId: Long, val waitTime: Long)
