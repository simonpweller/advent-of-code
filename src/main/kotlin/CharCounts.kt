fun charCounts(input: String): Map<Char, Int> =
    input.fold(mapOf()) { acc, c -> acc.plus(Pair(c, acc.getOrDefault(c, 0) + 1))}
