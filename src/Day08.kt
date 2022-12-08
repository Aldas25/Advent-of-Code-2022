fun main() {

    fun part1(r: Int, c: Int, grid: Array<IntArray>): Int {
        val visible = Array(r) { BooleanArray(c) { false } }

        for (row in 0 until r) {
            var highest = -1
            for (col in 0 until c) {
                if (grid[row][col] > highest) {
                    visible[row][col] = true
                    highest = grid[row][col]
                }
            }

            highest = -1
            for (col in (c-1) downTo 0) {
                if (grid[row][col] > highest) {
                    visible[row][col] = true
                    highest = grid[row][col]
                }
            }
        }

        for (col in 0 until c) {
            var highest = -1
             for (row in 0 until r) {
                if (grid[row][col] > highest) {
                    visible[row][col] = true
                    highest = grid[row][col]
                }
            }

            highest = -1
            for (row in (r-1) downTo 0) {
                if (grid[row][col] > highest) {
                    visible[row][col] = true
                    highest = grid[row][col]
                }
            }
        }

        return visible.sumOf { row -> row.count { it } }
    }

    fun calcVisible(x: Int, y: Int, dx: Int, dy: Int, r: Int, c: Int, grid: Array<IntArray>): Int {
        var ans = 0
        var i = x + dx
        var j = y + dy

        while ((i in 0 until r) && (j in 0 until c)) {
            ans++
            if (grid[i][j] >= grid[x][y]) break

            i += dx
            j += dy
        }

        return ans
    }

    fun scenicScore(x: Int, y: Int, r: Int, c: Int, grid: Array<IntArray>): Int {
        var ans = 1
        for (dir in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
            ans *= calcVisible(x, y, dir.first, dir.second, r, c, grid)
        }
        return ans
    }

    fun part2(r: Int, c: Int, grid: Array<IntArray>): Int {
        var ans = 0
        for (i in 0 until r)
            for (j in 0 until c)
                ans = maxOf(ans, scenicScore(i, j, r, c, grid))
        return ans
    }

    val filename =
//        "inputs/day08_sample"
        "inputs/day08"

    val input = readInput(filename)
    val grid = Array(input.size) { IntArray(input[0].length) }
    val r = grid.size
    val c = grid[0].size

    for (i in input.indices) {
        for (j in input[0].indices) {
            grid[i][j] = input[i][j].code
        }
    }

    println("Part 1: ${part1(r, c, grid)}")
    println("Part 2: ${part2(r, c, grid)}")
}