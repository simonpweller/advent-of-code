fun <T> List<List<T>>.transpose(): List<List<T>> {
    val ret: MutableList<List<T>> = ArrayList(this[0].size)
    val rowSize = this[0].size
    for (i in 0 until rowSize) {
        val col: MutableList<T> = ArrayList(this.size)
        for (row in this) {
            col.add(row[i])
        }
        ret.add(col)
    }
    return ret
}
