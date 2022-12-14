fun main() {

    fun dropSand(grid: Array<Array<Boolean>>): Pair<Int, Int> {
        var x = 500
        var y = 0

        while (true) {
            if (y >= 1000-1) break

            if (!grid[x][y+1])
                y++
            else if (!grid[x-1][y+1]) {
                x--
                y++
            } else if (!grid[x+1][y+1]) {
                x++
                y++
            } else
                break
        }

        return Pair(x, y)
    }

    fun printGrid(grid: Array<Array<Boolean>>) {
        println("grid:")
        for (y in 0..11) {
            for (x in 490..505) {
                if (grid[x][y])
                    print('#')
                else
                    print('.')
            }
            println()
        }
        println("-----")
    }

    fun part1(grid: Array<Array<Boolean>>): Int {
        var ans = 0
//        printGrid(grid)
        while (true) {
            val sandPos = dropSand(grid)
            if (sandPos.second >= 1000-1) break
            ans++
            grid[sandPos.first][sandPos.second] = true
            if (sandPos.first == 500 && sandPos.second == 0) break
//            printGrid(grid)
        }
        return ans
    }

    fun prepareGridPart2(grid: Array<Array<Boolean>>) {
        var maxY = 0
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                if (grid[x][y])
                    maxY = maxOf(maxY, y)
            }
        }

        maxY += 2
        for (x in grid[0].indices)
            grid[x][maxY] = true
    }

    fun part2(grid: Array<Array<Boolean>>): Int {
        prepareGridPart2(grid)
        return part1(grid)
    }

    fun markPath(grid: Array<Array<Boolean>>, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val dx = if (fromX == toX) 0
        else if (fromX > toX) -1
        else 1

        val dy = if (fromY == toY) 0
        else if (fromY > toY) -1
        else 1

        var i = fromX
        var j = fromY
        while (i != toX || j != toY) {
            grid[i][j] = true
            i += dx
            j += dy
        }
        grid[i][j] = true
    }

    fun constructGrid(input: List<String>): Array<Array<Boolean>> {
        val grid = Array(1000) { Array(1000) {false} }
        for (path in input) {
            val steps = path.split(" -> ")
            for (i in steps.indices) {
                if (i == 0) continue
                val from = steps[i-1].split(",")
                val to = steps[i].split(",")
                val fromX = from[0].toInt()
                val fromY = from[1].toInt()
                val toX = to[0].toInt()
                val toY = to[1].toInt()
                markPath(grid, fromX, fromY, toX, toY)
            }
        }
        return grid
    }

    val filename =
//        "inputs/day14_sample"
        "inputs/day14"

    val input = readInput(filename)
    val grid1 = constructGrid(input)
    val grid2 = constructGrid(input)

    println("Part 1: ${part1(grid1)}")
    println("Part 2: ${part2(grid2)}")
}