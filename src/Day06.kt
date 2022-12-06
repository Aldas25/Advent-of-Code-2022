fun main() {

    fun part1(input: String): Int {
        for (i in 3 until input.length) {
            val chars: MutableSet<Char> = mutableSetOf()
            for (j in (i-3)..i) {
                chars.add(input[j])
            }

//            println(" i = $i, chars = $chars")

            if (chars.size == 4)
                return i + 1
        }

        return -1
    }

    fun part2(input: String): Int {
        for (i in 13 until input.length) {
            val chars: MutableSet<Char> = mutableSetOf()
            for (j in (i-13)..i) {
                chars.add(input[j])
            }

//            println(" i = $i, chars = $chars")

            if (chars.size == 14)
                return i + 1
        }

        return -1
    }

    val filename =
//        "inputs\\day06_sample"
//        "inputs\\day06_sample2"
//        "inputs\\day06_sample3"
//        "inputs\\day06_sample4"
//        "inputs\\day06_sample5"
        "inputs\\day06"

    val input = readInput(filename)

    println("Part 1: ${part1(input[0])}")
    println("Part 2: ${part2(input[0])}")
}
