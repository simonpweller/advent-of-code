package y2021

import inputText
import java.util.Collections.max
import java.util.Collections.min

fun main() {
    val binary = inputText(2021, 16).toBinary()
    val topLevelPacket = parse(binary).packet
    println(topLevelPacket.totalVersionIds)
    println(topLevelPacket.value)
}

private fun parse(binary: String): ParseResult {
    var remaining = binary

    val version = remaining.take(3).toInt(2).also { remaining = remaining.drop(3) }
    val typeId = remaining.take(3).toInt(2).also { remaining = remaining.drop(3) }

    if (typeId == 4) {
        var tempNumber = ""
        while (remaining.first() == '1') {
            tempNumber += remaining.drop(1).take(4)
            remaining = remaining.drop(5)
        }
        tempNumber += remaining.drop(1).take(4)
        remaining = remaining.drop(5)
        return ParseResult(LiteralPacket(version, typeId, tempNumber.toLong(2)), remaining)
    } else {
        val subPackets = mutableListOf<Packet>()
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
        return ParseResult(OperatorPacket(version, typeId, subPackets), remaining)
    }
}

private data class ParseResult(val packet: Packet, val remaining: String)

private interface Packet {
    val totalVersionIds: Int
    val value: Long
}

private data class LiteralPacket(val version: Int, val typeId: Int, override val value: Long): Packet {
    override val totalVersionIds: Int
        get() = version
}

private data class OperatorPacket(val version: Int, val typeId: Int, val subPackets: List<Packet>): Packet {
    override val totalVersionIds: Int
        get() = version + subPackets.sumOf { it.totalVersionIds }
    override val value: Long
        get() {
            return when(typeId) {
                0 -> this.subPackets.map { it.value }.reduce { a, b -> a + b}
                1 -> this.subPackets.map { it.value }.reduce { a, b -> a * b}
                2 -> min(this.subPackets.map { it.value })
                3 -> max(this.subPackets.map { it.value })
                5 -> if (this.subPackets.first().value > this.subPackets.last().value) 1 else 0
                6 -> if (this.subPackets.first().value < this.subPackets.last().value) 1 else 0
                7 -> if (this.subPackets.first().value == this.subPackets.last().value) 1 else 0
                else -> throw IllegalArgumentException("Unknown operator $typeId")
            }
        }
}

private fun Char.toBinary() = this.toString().toInt(16).toString(2).padStart(4, '0')
private fun String.toBinary() = this.map(Char::toBinary).joinToString("")
