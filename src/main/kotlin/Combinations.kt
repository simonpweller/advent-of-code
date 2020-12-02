/**
 * Returns a list of lists, covering all possible permutations that include all items.
 * permutations(1, 2, 3) -> [[3, 2, 1], [2, 3, 1], [3, 1, 2], [1, 3, 2], [2, 1, 3], [1, 2, 3]]
 */

fun <T> permutations(items: List<T>): List<List<T>> {
    if (items.size == 1) {
        return listOf(items)
    }

    return items
        .map { currentItem -> permutations(items.minus(currentItem)).map {it.plus(currentItem)}}
        .flatten()
}

fun <T> permutations(items: Set<T>): List<List<T>> = permutations(items.toList())
fun permutations(items: IntRange): List<List<Int>> = permutations(items.toList())

/**
 * Returns a list of lists covering all combinations of items in the first and second list
 * combinations(listOf("a", "b"), listOf("c", "d")) -> [["a", "c"], ["a", "d"], ["b", "c"], ["b", "d"]]
 */
fun <T> combinations(firstList: List<T>, secondList: List<T>): List<List<T>> =
    firstList.map { first -> secondList.map { second -> listOf(first, second) } }.flatten()

/**
 * Returns a list of lists, covering all possible permutations, ignoring order. Duplicate items are treated as distinct
 * subLists(10, 5) -> [[], [5], [10], [5, 10]]
 * subLists(10, 5, 5) -> [[], [5], [5], [5, 5], [10], [5, 10], [5, 10], [5, 5, 10]]
 */
fun <T> subLists(items: List<T>): List<List<T>> {
    if (items.isEmpty()) {
        return listOf(items)
    }
    val first = items.first()
    val withoutFirst = items.minus(first)
    return subLists(withoutFirst).plus(subLists(withoutFirst).map { it.plus(first) })
}

/**
 * Returns a list of lists, covering all possible subLists of a given size
 * subListsOfSize(listOf(1, 2, 3), 1) -> [[1], [2], [3]]
 * subListsOfSize(listOf(1, 2, 3), 2) -> [[2, 1], [3, 1], [1, 2], [3, 2], [1, 3], [2, 3]]
 */
fun <T>subListsOfSize(list: List<T>, size: Int): List<List<T>> {
    if (size == 1) return list.map { listOf(it) }
    return list.map { subListsOfSize(list.minus(it), size - 1).map { subList -> subList.plus(it)} }.flatten()
}

private fun <T>subSetsOfSize(set: Set<T>, size: Int): Set<Set<T>> {
    if (size == 1) return set.map { setOf(it) }.toSet()
    return set.map { subSetsOfSize(set.minus(it), size - 1).map { subSet -> subSet.plus(it)} }.flatten().toSet()
}
