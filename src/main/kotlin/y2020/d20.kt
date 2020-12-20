package y2020

import inputChunks

fun main() {
    val pieces = inputChunks(2020, 20).map {
        Piece(
            it.lines().first().substringAfter(" ").dropLast(1).toLong(),
            it.lines().drop(1)
        )
    }
    println(part1(pieces))
}

private fun part1(pieces: List<Piece>) = pieces.filter { piece ->
    val otherPieces = pieces.minus(piece)
    val unmatchableEdges = piece.edges.count { edge -> otherPieces.count { it.fitsNextTo(edge) } == 0 }
    unmatchableEdges == 2
}.fold(1L) { product, piece -> product * piece.id }

private data class Piece(val id: Long, val image: List<String>) {
    val edges: List<String>
        get() {
            return listOf(
                image.first(),
                image.last(),
                image.map { it.first() }.joinToString(""),
                image.map { it.last() }.joinToString("")
            )
        }

    fun fitsNextTo(edge: String): Boolean {
        if (edge in edges) return true
        if (edge in edges.map { it.reversed() }) return true
        return false
    }
}
