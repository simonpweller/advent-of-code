package y2018

import inputChunks
import java.lang.Integer.min

fun main() {
    println(fight().second.sumBy { it.units })
    var boost = 34
    while (true) {
        val hasImmuneSystemWon = fight(boost).second.isEmpty()
        if (hasImmuneSystemWon) {
            println(fight(boost).first.sumBy { it.units })
            return
        }
        boost++
    }
}

private fun fight(boost: Int = 0): Pair<List<Group>, List<Group>> {
    var (immuneSystem, infection) = inputChunks(2018, 24).map {
        it.lines().drop(1).map { groupString -> Group.of(groupString) }
    }

    immuneSystem.forEach { it.damageAmount += boost }

    while (immuneSystem.isNotEmpty() && infection.isNotEmpty()) {
        val targetMap = getTargetMap(immuneSystem, infection).plus(getTargetMap(infection, immuneSystem))
        immuneSystem.plus(infection).sortedBy { -it.initiative }.forEach { attacker ->
            val target = targetMap[attacker]
            if (attacker.units <= 0 || target == null) return@forEach
            val killedUnits = min(attacker.potentialDamageTo(target) / target.hitPoints, target.units)
            target.units -= killedUnits
        }

        immuneSystem = immuneSystem.filter { it.units > 0 }
        infection = infection.filter { it.units > 0 }
    }

    return Pair(immuneSystem, infection)
}


private fun getTargetMap(
    attackers: List<Group>,
    defenders: List<Group>
): Map<Group, Group?> {
    val targetMap = mutableMapOf<Group, Group?>()
    val targetableDefenders = defenders.toMutableList()

    targetSelectionOrder(attackers).forEach { attacker ->
        val optimalTarget = targetableDefenders.sortedWith(
            compareBy(
                { -attacker.potentialDamageTo(it) },
                { -it.effectivePower },
                { -it.initiative })
        ).firstOrNull() ?: return@forEach
        val selectedTarget = if (attacker.potentialDamageTo(optimalTarget) > 0) optimalTarget else null
        if (selectedTarget != null) {
            targetMap[attacker] = if (attacker.potentialDamageTo(optimalTarget) > 0) optimalTarget else null
            targetableDefenders.remove(selectedTarget)
        }
    }
    return targetMap
}

private fun targetSelectionOrder(groups: List<Group>): List<Group> =
    groups.sortedWith(compareBy({ -it.effectivePower }, { -it.initiative }))

private class Group(
    var units: Int,
    val hitPoints: Int,
    val weaknesses: List<String>,
    val immunities: List<String>,
    var damageAmount: Int,
    val damageType: String,
    val initiative: Int
) {
    val effectivePower: Int
        get() = units * damageAmount

    fun potentialDamageTo(target: Group): Int = when {
        target.immunities.contains(damageType) -> 0
        target.weaknesses.contains(damageType) -> effectivePower * 2
        else -> effectivePower
    }

    companion object {
        fun of(groupString: String): Group {
            val weaknesses = "weak to ([\\w\\s,]*)".toRegex().find(groupString)?.groupValues?.get(1)?.split(", ")
            val immunities = "immune to ([\\w\\s,]*)".toRegex().find(groupString)?.groupValues?.get(1)?.split(", ")
            val units = groupString.substringBefore(" ").toInt()
            val hitPoints = groupString.substringAfter("with ").substringBefore(" hit points").toInt()
            val damageAmount = groupString.substringAfter("that does ").substringBefore(" ").toInt()
            val damageType = groupString.substringBefore(" damage").substringAfterLast(" ")
            val initiative = groupString.substringAfterLast(" ").toInt()
            return Group(
                units,
                hitPoints,
                weaknesses ?: emptyList(),
                immunities ?: emptyList(),
                damageAmount,
                damageType,
                initiative
            )
        }
    }
}
