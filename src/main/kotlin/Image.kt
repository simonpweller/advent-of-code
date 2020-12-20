data class Image(val rows: List<String>) {

    val variations: Set<Image>
        get() = rotations.plus(flippedVertically.rotations).plus(flippedHorizontally.rotations)

    val edges: List<String>
        get() = listOf(
            rows.first(),
            rows.map { it.last() }.joinToString(""),
            rows.last(),
            rows.map { it.first() }.joinToString(""),
        )

    private val flippedHorizontally: Image
        get() = Image(rows.map { it.reversed() })

    private val flippedVertically: Image
        get() = Image(rows.reversed())

    private val rotations: Set<Image>
        get() = setOf(
            this,
            rotated,
            rotated.rotated,
            rotated.rotated.rotated
        )


    val rotated: Image
        get() {
            val rotatedRows = Array(rows[0].length) { CharArray(rows.size) }
            for (i in rows.indices) {
                for (j in rows[0].indices) {
                    rotatedRows[j][rows.lastIndex - i] = rows[i][j]
                }
            }
            return Image(rotatedRows.map { it.joinToString("") })
        }

    override fun toString() = rows.joinToString("\n")
}
