package y2018

import inputLines
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val nanobots = inputLines(2018, 23).map(NanoBot::of)
    val strongestBot = nanobots.maxByOrNull { it.radius }!!
    val botsInRange = nanobots.map(NanoBot::coordinates).count { strongestBot.distanceTo(it) <= strongestBot.radius }
    println(botsInRange)

    val simplified = nanobots.map {
        NanoBot(
            Coordinates(
                it.coordinates.x / 1,
                it.coordinates.y / 1,
                it.coordinates.z / 1
            ), it.radius / 1
        )
    }
    simplified.forEach { println(it) }

//    val xMin = simplified.map { it.coordinates.x }.minOrNull()!!
//    val xMax = simplified.map { it.coordinates.x }.maxOrNull()!!
//    val yMin = simplified.map { it.coordinates.y }.minOrNull()!!
//    val yMax = simplified.map { it.coordinates.y }.maxOrNull()!!
//    val zMin = simplified.map { it.coordinates.z }.minOrNull()!!
//    val zMax = simplified.map { it.coordinates.z }.maxOrNull()!!
//
//    var max = 0
//    (xMin..xMax).forEach { x ->
//        (yMin..yMax).forEach { y ->
//            (zMin..zMax).forEach { z ->
//                val coordinates = Coordinates(x, y, z)
//                val count = simplified.count { it.distanceTo(coordinates) <= it.radius }
//                max = max(max, count)
//                println(max)
//            }
//        }
//    }
//    val coordinates = (xMin..xMax).flatMap { x ->
//        (yMin..yMax).flatMap { y ->
//            (zMin..zMax).map { z ->
//                Coordinates(x, y, z)
//            }
//        }
//    }

    var max = 0
    (21511200L..21512130L).forEach { x ->
        (13636590L..13636620).forEach { y ->
            (36482320L..36482350).forEach { z ->
                val coordinates = Coordinates(x, y, z)
                val count = simplified.count { it.distanceTo(coordinates) <= it.radius }
                max = max(max, count)
            }
        }
    }
    println(max)

    val coordinates =
        (21511200L..21512130L).flatMap { x ->
            (13636590L..13636620).flatMap { y ->
                (36482320L..36482350).map { z ->
                    Coordinates(x, y, z)
                }
            }
        }

//    1,2,3
//Coordinates(x=20, y=14, z=35)
//Coordinates(x=21, y=14, z=35)
//    913
//Coordinates(x=214, y=137, z=364)
//Coordinates(x=2150, y=1364, z=3647)
//916
//Coordinates(x=21511, y=13637, z=36482)
//Coordinates(x=215120, y=136365, z=364824)


    println(coordinates.filter { point -> simplified.count { it.distanceTo(point) <= it.radius } >= max - 1 }
        .map { it.x + it.y + it.z }.maxOrNull())
}

private data class NanoBot(val coordinates: Coordinates, val radius: Long) {
    companion object {
        fun of(string: String): NanoBot {
            val (x, y, z) = string.substringAfter("<").substringBefore(">").split(",").map(String::toLong)
            val r = string.substringAfterLast("=").toLong()
            return NanoBot(Coordinates(x, y, z), r)
        }
    }

    fun distanceTo(other: Coordinates): Long =
        with(coordinates) { abs(x - other.x) + abs(y - other.y) + abs(z - other.z) }
}

private data class Coordinates(val x: Long, val y: Long, val z: Long)
