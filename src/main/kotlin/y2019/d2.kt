package y2019

import inputText

fun main() {
    val intcode = inputText(2019, 2).split(",").map(String::toInt).toMutableList()
    intcode[1] = 12
    intcode[2] = 2
    (0..intcode.size - 4 step 4).forEach { index ->
        val (opcode, arg1, arg2, target) = intcode.subList(index, index + 4)
        when (opcode) {
            1 -> intcode[target] = intcode[arg1] + intcode[arg2]
            2 -> intcode[target] = intcode[arg1] * intcode[arg2]
            99 -> println(intcode[0]).also { return }
        }
    }

}
