import java.io.File

fun inputLines(year: Int, day: Int) = File("src/main/kotlin/y$year/d$day.txt").readLines()
fun inputText(year: Int, day: Int) = File("src/main/kotlin/y$year/d$day.txt").readText().trim()

