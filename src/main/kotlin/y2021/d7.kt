package y2021

import inputText
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.abs

fun main() {
    val startingLocations = inputText(2021, 7).split(",").map(String::toInt)
    println(calculateOptimalFuelCost(startingLocations, ::simpleFuelConsumption))
    println(calculateOptimalFuelCost(startingLocations, ::advancedFuelConsumption))
}

private fun calculateOptimalFuelCost(startingLocations: List<Int>, fuelCostFunction: (distance: Int) -> Int) =
    targetOptions(startingLocations)
        .map { target -> distances(startingLocations, target) }
        .map { distances -> distances.sumOf(fuelCostFunction) }
        .minOrNull()

private fun targetOptions(startingLocations: List<Int>) = min(startingLocations)..max(startingLocations)
private fun distances(startingLocations: List<Int>, target: Int) = startingLocations.map { abs(it - target) }
private fun simpleFuelConsumption(distance: Int) = distance
private fun advancedFuelConsumption(distance: Int) = (distance * (distance + 1)) / 2
