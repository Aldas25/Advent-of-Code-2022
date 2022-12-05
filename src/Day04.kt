fun main() {

    fun contains(firstElf: List<Int>, secondElf: List<Int>): Boolean {
        return firstElf[0] <= secondElf[0] && secondElf[1] <= firstElf[1]
    }

    fun overlaps(firstElf: List<Int>, secondElf: List<Int>): Boolean {
        return !(firstElf[0] > secondElf[1] || secondElf[0] > firstElf[1])
    }

    fun part1(input: List<String>): Int {
        var ans = 0

        for (s in input) {
            val ranges = s.split(",")
            val firstElf = ranges[0].split("-").map { x -> x.toInt() }
            val secondElf = ranges[1].split("-").map { x -> x.toInt() }

            if (contains(firstElf, secondElf) || contains(secondElf, firstElf)) {
                ans++
            }
        }

        return ans
    }

    fun part2(input: List<String>): Int {
        var ans = 0

        for (s in input) {
            val ranges = s.split(",")
            val firstElf = ranges[0].split("-").map { x -> x.toInt() }
            val secondElf = ranges[1].split("-").map { x -> x.toInt() }

            if (overlaps(firstElf, secondElf)) {
                ans++
            }
        }
        return ans
    }

    val filename =
//        "inputs\\day04_sample"
        "inputs\\day04"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
