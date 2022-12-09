import kotlin.math.abs

fun main() {

    val dirs = mapOf(
        'U' to Pair(0, 1),
        'D' to Pair(0, -1),
        'R' to Pair(1, 0),
        'L' to Pair(-1, 0)
    )

    fun moveTail (head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        var dx = head.first - tail.first
        var dy = head.second - tail.second
        if (maxOf(abs(dx), abs(dy)) <= 1)
            return tail

        dx = minOf(1, maxOf(-1, dx))
        dy = minOf(1, maxOf(-1, dy))

        return Pair(tail.first + dx, tail.second + dy)
    }

    fun part1(input: List<String>): Int {
        var head = Pair(0, 0)
        var tail = Pair(0, 0)
        val positions = mutableSetOf(tail)

        for (s in input) {
            val move = s[0]
            val count = s.substring(2).toInt()
            repeat(count) {
                head = Pair(
                    head.first + dirs[move]!!.first,
                    head.second + dirs[move]!!.second
                )
                tail = moveTail(head, tail)

//                println(" head: $head, tail: $tail")

                positions.add(tail)
            }
        }

        return positions.size
    }

    fun part2(input: List<String>): Int {
        val rope = Array(10) { Pair(0, 0) }
        val positions = mutableSetOf(rope[9])

        for (s in input) {
            val move = s[0]
            val count = s.substring(2).toInt()
            repeat(count) {
                rope[0] = Pair(
                    rope[0].first + dirs[move]!!.first,
                    rope[0].second + dirs[move]!!.second
                )
                for (i in 1..9) {
                    rope[i] = moveTail(rope[i-1], rope[i])
                }

//                println(" head: $head, tail: $tail")

                positions.add(rope[9])
            }
        }

        return positions.size
    }

    val filename =
//        "inputs/day09_sample"
//        "inputs/day09_sample2"
        "inputs/day09"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}