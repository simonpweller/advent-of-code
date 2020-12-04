package y2020

import inputLines

fun main() {
    val lines = inputLines(2020, 4)
    val passportStrings = parsePasswordStrings(lines)
    val passports = passportStrings.map(::Passport)

    println(passports.filter(Passport::hasAllRequiredFields).size)
    println(passports.filter(Passport::isValid).size)
}

class Passport(string: String) {
    private val fields: Map<Field, String> = string.split(" ").fold(mapOf()) { map, fieldString ->
        val (key, value) = fieldString.split(":")
        map.plus(Field.valueOf(key.toUpperCase()) to value)
    }

    private val requiredFields = Field.values().toSet().minus(Field.CID)

    val hasAllRequiredFields: Boolean
        get() = requiredFields.all { this.has(it) }

    val isValid: Boolean
        get() = requiredFields.all { this.hasValid(it) }

    private fun has(field: Field): Boolean = fields.containsKey(field)

    private fun hasValid(field: Field): Boolean = has(field) && field.isValid(fields.getValue(field))

    enum class Field {
        BYR,
        IYR,
        EYR,
        HGT,
        HCL,
        ECL,
        PID,
        CID;

        fun isValid(value: String): Boolean {
            return when (this) {
                BYR -> value.toInt() in 1920..2002
                IYR -> value.toInt() in 2010..2020
                EYR -> value.toInt() in 2020..2030
                HGT ->
                    value.endsWith("cm") && value.length == 5 && value.substring(0, 3).toInt() in 150..193 ||
                            value.endsWith("in") && value.length == 4 && value.substring(0, 2).toInt() in 59..76
                HCL -> "#[0-9|a-f]{6}".toRegex().matches(value)
                ECL -> value in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                PID -> "\\d{9}".toRegex().matches(value)
                CID -> true
            }
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







