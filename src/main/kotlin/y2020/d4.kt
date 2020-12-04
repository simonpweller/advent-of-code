package y2020

import inputLines
import jdk.jshell.spi.ExecutionControl.*

fun main() {
    val lines = inputLines(2020, 4)
    val passportStrings = parsePasswordStrings(lines)
    val passports = passportStrings.map(::Passport)

    println(passports.filter(Passport::hasAllRequiredFields).size)
    println(passports.filter(Passport::isValid).size)
}

class Passport(string: String) {
    private val fields: Map<String, String> = string.split(" ").fold(mapOf()) { map, fieldString ->
        val (key, value) = fieldString.split(":")
        map.plus(key to value)
    }

    private val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    val hasAllRequiredFields: Boolean
        get() = requiredFields.all { this.has(it) }

    val isValid: Boolean
        get() = requiredFields.all { this.hasValid(it) }

    private fun has(fieldName: String): Boolean = fields.containsKey(fieldName)

    private fun hasValid(fieldName: String): Boolean {
        val value = fields[fieldName] ?: return false
        return try {
            when (fieldName) {
                "byr" -> value.toInt() in 1920..2002
                "iyr" -> value.toInt() in 2010..2020
                "eyr" -> value.toInt() in 2020..2030
                "hgt" ->
                    value.endsWith("cm") && value.length == 5 && value.substring(0, 3).toInt() in 150..193 ||
                            value.endsWith("in") && value.length == 4 && value.substring(0, 2).toInt() in 59..76
                "hcl" -> "#[0-9|a-f]{6}".toRegex().matches(value)
                "ecl" -> value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                "pid" -> "\\d{9}".toRegex().matches(value)
                else -> throw NotImplementedException("Unknown field")
            }
        } catch (e: Exception) {
            false
        }
    }
}

private fun parsePasswordStrings(lines: List<String>): MutableList<String> {
    val passportStrings = mutableListOf<String>()
    var current = ""
    lines.forEach { line ->
        if (line.isBlank()) {
            passportStrings.add(current)
            current = ""
        } else {
            if (current.isNotBlank()) current += " "
            current += line
        }
    }
    passportStrings.add(current)
    return passportStrings
}







