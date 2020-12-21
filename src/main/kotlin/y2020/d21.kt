package y2020

import inputLines

fun main() {
    val recipes = inputLines(2020, 21).map { recipe ->
        val ingredients = recipe.substringBefore(" (").split(" ")
        val allergens = recipe.substringAfter("(contains ").substringBefore(")").split(", ")
        Recipe(ingredients, allergens)
    }

    val ingredientsForAllergens = mapAllergensToIngredients(recipes)
    println(part1(recipes, ingredientsForAllergens))
    println(part2(ingredientsForAllergens))
}

private fun part1(
    recipes: List<Recipe>,
    ingredientsForAllergens: MutableMap<String, String>
): Int {
    val allIngredients = recipes.fold(setOf<String>()) { set, recipe -> set.union(recipe.ingredients) }
    val nonAllergens = allIngredients.minus(ingredientsForAllergens.values)
    return recipes.sumBy { it.ingredients.count { ingredient -> ingredient in nonAllergens } }
}

private fun part2(ingredientsForAllergens: MutableMap<String, String>): String =
    ingredientsForAllergens.entries.sortedBy { it.key }.joinToString(",") { it.value }


private fun mapAllergensToIngredients(recipes: List<Recipe>): MutableMap<String, String> {
    val possibleIngredientsForAllergens = mutableMapOf<String, Set<String>>()
    recipes.forEach { recipe ->
        recipe.allergens.forEach { allergen ->
            if (allergen in possibleIngredientsForAllergens.keys) {
                possibleIngredientsForAllergens[allergen] =
                    recipe.ingredients.intersect(possibleIngredientsForAllergens.getValue(allergen))
            } else {
                possibleIngredientsForAllergens[allergen] = recipe.ingredients.toSet()
            }
        }
    }

    val ingredientsForAllergens = mutableMapOf<String, String>()
    while (possibleIngredientsForAllergens.values.any { it.size == 1 }) {
        val solvable = possibleIngredientsForAllergens.entries.first { it.value.size == 1 }
        ingredientsForAllergens[solvable.key] = solvable.value.first()
        possibleIngredientsForAllergens.remove(solvable.key)
        possibleIngredientsForAllergens.entries.forEach {
            possibleIngredientsForAllergens[it.key] = it.value.minus(solvable.value.first())
        }
    }
    return ingredientsForAllergens
}

data class Recipe(val ingredients: List<String>, val allergens: List<String>)
