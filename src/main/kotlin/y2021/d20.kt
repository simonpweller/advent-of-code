package y2021

import inputChunks

fun main() {
    val (algorithm, input) = inputChunks(2021, 20)
    val image = input.split(System.lineSeparator()).map { line -> line.toList() }.let(::Image)
    println(pixelCountAfterEnhancing(image, algorithm, 2))
    println(pixelCountAfterEnhancing(image, algorithm, 50))
}

private fun pixelCountAfterEnhancing(image: Image, algorithm: String, repeat: Int): Int {
    val enhancedImage = generateSequence(image) { it.enhance(algorithm) }.drop(1).take(repeat).last()
    return enhancedImage.pixels.sumOf { line -> line.count { it == '#' } }
}

private data class Image(val pixels: List<List<Char>>, val filler: Char = '.') {
    private val width = pixels.size
    private val height = pixels.first().size

    fun enhance(algorithm: String): Image {
        val expanded = expand(filler)
        val nextPixels = (0 until expanded.width).map { y ->
            (0 until expanded.height).map { x ->
                val index = Pixel(x, y).enhancePixels.map { expanded.pixelValue(it, filler) }.joinToString("").toInt(2)
                algorithm[index]
            }
        }
        return Image(
            nextPixels,
            (if (filler == '.') algorithm.first() else algorithm.last()))
    }

    private fun expand(filler: Char): Image = Image(
        listOf(List(width + 2) { filler })
            .plus(pixels.map { line -> listOf(filler) + line + listOf(filler) })
            .plus(listOf(List(width + 2) { filler }))
    )

    private fun pixelValue(pixel: Pixel, filler: Char): Int =
        pixels.getOrElse(pixel.y) { emptyList() }.getOrElse(pixel.x) { filler }
            .let { if (it == '#') 1 else 0 }
}

private data class Pixel(val x: Int, val y: Int) {
    val enhancePixels: List<Pixel>
        get() = (-1..1).flatMap { yOffset -> (-1..1).map { xOffset -> Pixel(x + xOffset, y + yOffset) } }
}
