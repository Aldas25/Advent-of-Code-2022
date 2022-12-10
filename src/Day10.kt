import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val cycles = listOf(20, 60, 100, 140, 180, 220)
        var ans = 0
        var x = 1
        var cycleNumber = 0

        fun oneCycle() {
            cycleNumber++
            if (cycleNumber in cycles)
                ans += cycleNumber * x
        }

        for (s in input) {
            if (s[0] == 'n') {
                // noop
                oneCycle()
                continue
            }
            // else "addx"
            val toAdd = s.split(" ")[1].toInt()
            repeat(2) { oneCycle() }
            x += toAdd
        }

        return ans
    }

    fun part2(input: List<String>) {
        var x = 1
        var crtX = -1
        var crtY = 0
        val grid = Array(6) { Array(40) { false } }

        fun printGrid() {
            for (i in 0..5) {
                for (j in 0..39) {
                    print(if (grid[i][j]) '#' else '.')
                }
                print('\n')
            }
        }

        fun oneCycle() {
            crtX++
            if (crtX >= grid[0].size) {
                crtX = 0
                crtY++
            }
            if (abs(x - crtX) <= 1)
                grid[crtY][crtX] = true
        }

        for (s in input) {
            if (s[0] == 'n') {
                // noop
                oneCycle()
                continue
            }
            // else "addx"
            val toAdd = s.split(" ")[1].toInt()
            repeat(2) { oneCycle() }
            x += toAdd
        }

        printGrid()
    }

    val filename =
//        "inputs/day10_sample"
        "inputs/day10"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2:")
    part2(input)
}