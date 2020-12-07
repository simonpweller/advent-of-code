import java.io.File

fun inputLines(year: Int, day: Int) = File("src/main/kotlin/y$year/d$day.txt").readLines()
fun inputText(year: Int, day: Int) = File("src/main/kotlin/y$year/d$day.txt").readText().trim()
fun inputChunks(year: Int, day: Int) =
    File("src/main/kotlin/y$year/d$day.txt").readText().trim().split("\r\n\r\n|\n\n".toRegex())

