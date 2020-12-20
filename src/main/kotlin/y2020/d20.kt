package y2020

import Image
import inputChunks

fun main() {
    val pieces = inputChunks(2020, 20).map {
        Piece(
            it.lines().first().substringAfter(" ").dropLast(1).toLong(),
            Image(it.lines().drop(1))
        )
    }
    println(part1(pieces))
    println(part2(pieces))
}



private fun part1(pieces: List<Piece>) =
    pieces.filter { piece -> piece.unmatchableEdges(pieces) == 2 }.fold(1L) { product, piece -> product * piece.id }

private fun part2(pieces: List<Piece>): Int {
    // solve puzzle
    val assembled = assemble(pieces)

    // remove edges
    val edgesRemoved = assembled.map { row -> row.map { piece ->
        val rows = piece.image.rows
        Piece(piece.id, Image(rows.drop(1).dropLast(1).map { row -> row.drop(1).dropLast(1) })) }
    }

    // join into single image
    val image = Image(edgesRemoved.flatMap { rowOfPieces ->
        rowOfPieces.map { it.image.rows }.reduce { acc, curr -> acc.mapIndexed { index, s -> s + curr[index] } }
    })

    // rotate and flip to find sea monsters
    val seaMonsterCount = image.variations.maxByOrNull { it.countMonsters }?.countMonsters ?: throw IllegalStateException("No monsters!")

    // calculate roughness of sea
    val hashes = image.rows.joinToString("").count { it == '#' }
    val monsterHashes = seaMonsterCount * 15
    return hashes - monsterHashes
}

private fun assemble(pieces: List<Piece>): List<List<Piece>> {
    val cornerPieces = pieces.filter { piece -> piece.unmatchableEdges(pieces) == 2 }
    val startingPiece = cornerPieces.first()
    val remainingPieces = pieces.minus(startingPiece).toMutableList()

    val puzzle = (0 until 12).map { mutableListOf<Piece>() }.toList()

    (0 until 12).forEach { rowIndex ->
        (0 until 12).forEach { colIndex ->
            if (rowIndex == 0 && colIndex == 0) {
                puzzle[0].add(
                    Piece(
                        startingPiece.id,
                        startingPiece.image.rotated
                    )
                ) // figured the right rotation out manually
                remainingPieces.remove(startingPiece)
            } else if (colIndex == 0) {
                val edgeToMatch = puzzle[rowIndex - 1][colIndex].image.edges[2]
                val matchingPieces = remainingPieces.filter { it.fitsNextTo(edgeToMatch) }
                if (matchingPieces.size != 1) throw IllegalStateException("Multiple matching pieces: ${matchingPieces.size}")
                val matchingPiece = matchingPieces.first()
                val matchingVariations = matchingPiece.image.variations.filter { it.edges[0] == edgeToMatch }
                if (matchingVariations.size != 1) throw IllegalStateException("Multiple matching variations")
                val matchingVariation = matchingVariations.first()
                puzzle[rowIndex].add(Piece(matchingPiece.id, matchingVariation))
                remainingPieces.remove(matchingPiece)
            } else {
                val edgeToMatch = puzzle[rowIndex][colIndex - 1].image.edges[1]
                val matchingPieces = remainingPieces.filter { it.fitsNextTo(edgeToMatch) }
                if (matchingPieces.size != 1) throw IllegalStateException("Multiple matching pieces: ${matchingPieces.size}")
                val matchingPiece = matchingPieces.first()
                val matchingVariations = matchingPiece.image.variations.filter { it.edges[3] == edgeToMatch }
                if (matchingVariations.size != 1) throw IllegalStateException("Multiple matching variations")
                val matchingVariation = matchingVariations.first()
                puzzle[rowIndex].add(Piece(matchingPiece.id, matchingVariation))
                remainingPieces.remove(matchingPiece)
            }
        }
    }

    return puzzle
}

private data class Piece(val id: Long, val image: Image) {
    fun fitsNextTo(edge: String): Boolean {
        if (edge in image.edges) return true
        if (edge in image.edges.map { it.reversed() }) return true
        return false
    }

    fun unmatchableEdges(pieces: List<Piece>): Int =
        image.edges.count { edge -> pieces.minus(this).count { it.fitsNextTo(edge) } == 0 }
}

private val Image.countMonsters: Int
    get() {

        fun isSeaMonster(window: List<CharSequence>): Boolean {
            val seaMonster = listOf(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   ",
            )
            (0 .. 2).forEach { windowRow ->
                (0 .. 19).forEach { windowCol ->
                    if (seaMonster[windowRow][windowCol] == '#' && window[windowRow][windowCol] != '#') return false
                }
            }
            return true
        }
        var count = 0
        (0 .. rows.lastIndex - 2).forEach { rowIndex ->
            (0 .. rows.first().lastIndex - 19).forEach { colIndex ->
                val window = rows.subList(rowIndex, rowIndex + 3).map { row -> row.subSequence(colIndex, colIndex + 20) }
                if (isSeaMonster(window)) count++
            }
        }
        return count
    }
