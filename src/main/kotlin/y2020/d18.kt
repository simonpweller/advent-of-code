package y2020

import inputLines

fun main() {
    val expressions = inputLines(2020, 18)
    println(expressions.map { evaluate(it) }.sum())
    println(expressions.map { evaluatePart2(it) }.sum())
}

fun evaluate(expression: String): Long {
    if (expression.contains('(')) {
        val startOfInner = expression.lastIndexOf('(')
        val endOfInner = expression.indexOf(')', startOfInner)
        val inner = expression.substring(startOfInner + 1, endOfInner)
        return evaluate(expression.substring(0, startOfInner) + evaluate(inner) + expression.substring(endOfInner + 1))
    }
    val tokens = expression.split(" ")
    var res = tokens.first().toLong()
    val operations = tokens.drop(1).chunked(2).map { Operation(it[0], it[1].toLong()) }
    operations.forEach { operation ->
        res = if (operation.operator == "*") res * operation.argument else res + operation.argument
    }
    return res
}

fun evaluatePart2(expression: String): Long {
    if (expression.contains('(')) {
        val startOfInner = expression.lastIndexOf('(')
        val endOfInner = expression.indexOf(')', startOfInner)
        val inner = expression.substring(startOfInner + 1, endOfInner)
        return evaluatePart2(expression.substring(0, startOfInner) + evaluatePart2(inner) + expression.substring(endOfInner + 1))
    }
    if (expression.contains('+')) {
        val tokens = expression.split(" ")
        val indexOfFirstAddition = tokens.indexOf("+")
        return evaluatePart2(
            listOf(
                tokens.subList(0, indexOfFirstAddition - 1).joinToString(" "),
                (tokens[indexOfFirstAddition - 1].toLong() + tokens[indexOfFirstAddition + 1].toLong()),
                tokens.subList(indexOfFirstAddition + 2, tokens.size).joinToString(" ")
            ).filter { it != "" }.joinToString(" ")
        )
    }
    val tokens = expression.split(" ")
    var res = tokens.first().toLong()
    val operations = tokens.drop(1).chunked(2).map { Operation(it[0], it[1].toLong()) }
    operations.forEach { operation ->
        res = if (operation.operator == "*") res * operation.argument else res + operation.argument
    }
    return res
}


private class Operation(val operator: String, val argument: Long)
