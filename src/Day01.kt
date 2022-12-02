import com.sun.jdi.IntegerType

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        var currentElf = 0

        for (s in input) {
            if (s.isBlank()) {
                currentElf = 0
            } else {
                currentElf += s.toInt()
            }

            answer = maxOf(answer, currentElf)
        }

        return answer
    }

    fun part2(input: List<String>): Int {
        var currentElf = 0
        var listOfElfs: List<Int> = emptyList()

        for (s in input) {
            if (s.isBlank()) {
                listOfElfs = listOfElfs.plus(currentElf)
                currentElf = 0
            } else {
                currentElf += s.toInt()
            }
        }
        listOfElfs = listOfElfs.plus(currentElf)
        listOfElfs = listOfElfs.sortedDescending()

        val answer = listOfElfs[0] + listOfElfs[1] + listOfElfs[2]

        return answer
    }

    val filename =
//        "inputs\\day01_sample"
        "inputs\\day01"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
