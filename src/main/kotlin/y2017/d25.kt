package y2017

fun main() {
    val stateMachine = StateMachine()
    stateMachine.fastForward(12629077)
    println(stateMachine.checksum())
}

class StateMachine {
    private val tape = hashSetOf<Int>()
    private var state = 'A'
    private var slot = 0

    fun fastForward(steps: Int) {
        repeat(steps) { this.next() }
    }

    fun checksum() = tape.size

    fun next() {
        when (state) {
            'A' ->
                state = (if (!tape.contains(slot)) {
                    tape.add(slot)
                    slot++
                    'B'
                } else {
                    tape.remove(slot)
                    slot--
                    'B'
                })
            'B' ->
                state = (if (!tape.contains(slot)) {
                    tape.remove(slot)
                    slot++
                    'C'
                } else {
                    tape.add(slot)
                    slot--
                    'B'
                })
            'C' ->
                state = if (!tape.contains(slot)) {
                    tape.add(slot)
                    slot++
                    'D'
                } else {
                    tape.remove(slot)
                    slot--
                    'A'
                }
            'D' ->
                state = if (!tape.contains(slot)) {
                    tape.add(slot)
                    slot--
                    'E'
                } else {
                    tape.add(slot)
                    slot--
                    'F'
                }
            'E' ->
                state = if (!tape.contains(slot)) {
                    tape.add(slot)
                    slot--
                    'A'
                } else {
                    tape.remove(slot)
                    slot--
                    'D'
                }
            'F' ->
                state = if (!tape.contains(slot)) {
                    tape.add(slot)
                    slot++
                    'A'
                } else {
                    tape.add(slot)
                    slot--
                    'E'
                }
        }
    }
}
