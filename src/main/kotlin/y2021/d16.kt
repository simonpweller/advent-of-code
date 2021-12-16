package y2021

import inputText

fun main() {
    val binary = inputText(2021, 16).toBinary()
    val topLevelPacket = parse(binary).first
    println(topLevelPacket.totalVersionIds)
}

private fun parse(binary: String): Pair<Packet, String> {
    var remaining = binary

    val version = remaining.take(3).toInt(2).also { remaining = remaining.drop(3) }
    val typeId = remaining.take(3).toInt(2).also { remaining = remaining.drop(3) }
    val subPackets = mutableListOf<Packet>()


    if (typeId == 4) { // literal value
        while (remaining.first() == '1') {
            remaining = remaining.drop(5)
        }
        remaining = remaining.drop(5) // last group
    } else {
        val lengthType = remaining.take(1).toInt(2).also { remaining = remaining.drop(1) }
        if (lengthType == 0) {
            val length = remaining.take(15).toInt(2).also { remaining = remaining.drop(15) }
            var subPacketBinary = remaining.take(length).also { remaining = remaining.drop(length) }
            while (subPacketBinary.isNotEmpty()) {
                val (subPacket, remainingSubPacketBinary) = parse(subPacketBinary)
                subPacketBinary = remainingSubPacketBinary
                subPackets.add(subPacket)
            }
        } else {
            val subPacketCount = remaining.take(11).toInt(2).also { remaining = remaining.drop(11) }
            while (subPackets.size < subPacketCount) {
                val (subPacket, nextRemaining) = parse(remaining)
                remaining = nextRemaining
                subPackets.add(subPacket)
            }
        }
    }

    return Pair(Packet(version, typeId, subPackets), remaining)
}

private data class Packet(val version: Int, val typeId: Int, val subPackets: List<Packet>) {
    val totalVersionIds: Int
        get() = version + subPackets.sumOf { it.totalVersionIds }
}
private fun Char.toBinary() = this.toString().toInt(16).toString(2).padStart(4, '0')
private fun String.toBinary() = this.map(Char::toBinary).joinToString("")


