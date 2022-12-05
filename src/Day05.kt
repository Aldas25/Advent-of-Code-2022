fun main() {

    fun part1(input: List<String>, stacks: Array<ArrayDeque<Char>>): String {
        var ans = ""

        for (str in input) {
            val parts = str.split(" ")
            val quantity = parts[1].toInt()
            val from = parts[3].toInt()
            val to = parts[5].toInt()

            repeat(quantity) {
                stacks[to-1].addLast(stacks[from-1].removeLast())
            }
        }

        for (stack in stacks) {
            ans += stack.last()
        }

        return ans
    }

    fun part2(input: List<String>, stacks: Array<ArrayDeque<Char>>): String {
        for (str in input) {
            val parts = str.split(" ")
            val quantity = parts[1].toInt()
            val from = parts[3].toInt()
            val to = parts[5].toInt()

            val newStack: ArrayDeque<Char> = ArrayDeque()

            repeat(quantity) {
                newStack.addLast(stacks[from-1].removeLast())
            }

            repeat(quantity) {
                stacks[to-1].addLast(newStack.removeLast())
            }
        }

        var ans = ""

        for (stack in stacks) {
            ans += stack.last()
        }

        return ans
    }

    fun getStacks(filename: String): Array<ArrayDeque<Char>> {
        return if (filename == "inputs\\day05_sample")
            arrayOf(
                ArrayDeque(listOf('Z', 'N')),
                ArrayDeque(listOf('M', 'C', 'D')),
                ArrayDeque(listOf('P'))
            ) else  arrayOf(
            ArrayDeque(listOf('H', 'T', 'Z', 'D')),
            ArrayDeque(listOf('Q', 'R', 'W', 'T', 'G', 'C', 'S')),
            ArrayDeque(listOf('P', 'B', 'F', 'Q', 'N', 'R', 'C', 'H')),
            ArrayDeque(listOf('L', 'C', 'N', 'F', 'H', 'Z')),
            ArrayDeque(listOf('G', 'L', 'F', 'Q', 'S')),
            ArrayDeque(listOf('V', 'P', 'W', 'Z', 'B', 'R', 'C', 'S')),
            ArrayDeque(listOf('Z', 'F', 'J')),
            ArrayDeque(listOf('D', 'L', 'V', 'Z', 'R', 'H', 'Q')),
            ArrayDeque(listOf('B', 'H', 'G', 'N', 'F', 'Z', 'L', 'D'))
        )
    }

    val filename =
//        "inputs\\day05_sample"
        "inputs\\day05"

    val input = readInput(filename)

    println("Part 1: ${part1(input, getStacks(filename))}")
    println("Part 2: ${part2(input, getStacks(filename))}")
}
