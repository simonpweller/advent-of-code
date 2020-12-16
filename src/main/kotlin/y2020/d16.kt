package y2020

import inputChunks

fun main() {
    val inputChunks = inputChunks(2020, 16)
    val ticketRules = inputChunks[0].lines().map(TicketRule.Companion::of)
    val yourTicket = inputChunks[1].lines().drop(1).map { it.split(",").map(String::toInt) }.first()
    val nearbyTickets = inputChunks[2].lines().drop(1).map { it.split(",").map(String::toInt) }
    val validRanges = ticketRules.flatMap { listOf(it.highRange, it.lowRange) }

    println(part1(nearbyTickets, validRanges))
    println(part2(nearbyTickets, validRanges, ticketRules, yourTicket))
}


private data class TicketRule(val fieldName: String, val lowRange: IntRange, val highRange: IntRange) {
    companion object {
        fun of(it: String): TicketRule {
            val fieldName = it.substringBefore(":")
            val low = it.substringAfter(": ").substringBefore(" or ").split("-")
                .let { range -> IntRange(range.first().toInt(), range.last().toInt()) }
            val high = it.substringAfter(" or ").split("-")
                .let { range -> IntRange(range.first().toInt(), range.last().toInt()) }
            return TicketRule(fieldName = fieldName, lowRange = low, highRange = high)
        }
    }
}

private fun part1(
    nearbyTickets: List<List<Int>>,
    validRanges: List<IntRange>
) = nearbyTickets.flatten().filter { value -> validRanges.none { value in it } }.sum()

private fun part2(
    nearbyTickets: List<List<Int>>,
    validRanges: List<IntRange>,
    ticketRules: List<TicketRule>,
    yourTicket: List<Int>
): Long {
    val validNearbyTickets = nearbyTickets.filter { ticket ->
        ticket.all { value -> validRanges.any { value in it } }
    }

    val potentialMatches = mutableMapOf<String, List<Int>>()
    ticketRules.forEach { ticketRule ->
        potentialMatches[ticketRule.fieldName] = ticketRules.indices.filter { fieldIndex ->
            validNearbyTickets.all { ticket -> ticket[fieldIndex] in ticketRule.lowRange || ticket[fieldIndex] in ticketRule.highRange }
        }
    }

    val matches = mutableMapOf<String, Int>()
    while (potentialMatches.isNotEmpty()) {
        val match = potentialMatches.entries.first { it.value.size == 1 }
        matches[match.key] = match.value.first()
        potentialMatches.remove(match.key)
        potentialMatches.entries.forEach { it.setValue(it.value.filter { it != match.value.first() }) }
    }
    return matches.filter { it.key.startsWith("departure") }.values.map { yourTicket[it] }.fold(1L) { a, b -> a * b }
}
