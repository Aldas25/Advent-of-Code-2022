fun main() {


    fun part1(grid: Array<Array<Boolean>>): Int {
        val directions = mutableListOf(0, 1, 2, 3)

        fun simulateRound() {

            val proposes = Array(grid.size) { Array(grid[0].size) { Pair(-1, -1) } }
            val proposedByCnt = Array(grid.size) { Array(grid[0].size) { 0 } }
            val newGrid = Array(grid.size) { Array(grid[0].size) { false } }

            fun checkNorth(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j-1] || grid[i-1][j] || grid[i-1][j+1]) return
                proposes[i][j] = Pair(i-1, j)
            }

            fun checkSouth(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i+1][j-1] || grid[i+1][j] || grid[i+1][j+1]) return
                proposes[i][j] = Pair(i+1, j)
            }

            fun checkWest(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j-1] || grid[i][j-1] || grid[i+1][j-1]) return
                proposes[i][j] = Pair(i, j-1)
            }

            fun checkEast(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j+1] || grid[i][j+1] || grid[i+1][j+1]) return
                proposes[i][j] = Pair(i, j+1)
            }

            for (i in grid.indices) {
                for (j in grid[0].indices) {
                    if (i == 0 || j == 0 || i == grid.size-1 || j == grid[0].size-1)
                        continue
                    if (!grid[i][j]) continue

                    var cnt = 0
                    for (ni in (i-1)..(i+1)) for (nj in (j-1)..(j+1)) {
                        if (ni == i && nj == j) continue
                        if (ni < 0 || nj < 0 || ni >= grid.size || nj >= grid[0].size) continue
                        if (grid[ni][nj]) cnt++
                    }

                    if (cnt == 0) continue

                    for (dir in directions) {
                        when (dir) {
                            0 -> checkNorth(i, j)
                            1 -> checkSouth(i, j)
                            2 -> checkWest(i, j)
                            else -> checkEast(i, j)
                        }
                    }

                    if (proposes[i][j].first != -1) {
                        val p = proposes[i][j]
                        proposedByCnt[p.first][p.second]++
                    }
                }
            }

            for (i in grid.indices) for (j in grid[0].indices) {
                if (!grid[i][j]) continue
                val p = proposes[i][j]
                if (p.first == -1) newGrid[i][j] = true
                else if (proposedByCnt[p.first][p.second] > 1) newGrid[i][j] = true
                else newGrid[p.first][p.second] = true
            }

            for (i in grid.indices) for (j in grid[0].indices)
                grid[i][j] = newGrid[i][j]
        }

        fun printGrid() {
            println("--- Grid:")

            var mnX = Integer.MAX_VALUE
            var mxX = Integer.MIN_VALUE
            var mnY = Integer.MAX_VALUE
            var mxY = Integer.MIN_VALUE
            for (i in grid.indices) for (j in grid[0].indices) {
                if (!grid[i][j]) continue
                mnX = minOf(mnX, i)
                mxX = maxOf(mxX, i)
                mnY = minOf(mnY, j)
                mxY = maxOf(mxY, j)
            }

            var ans = 0
            for (i in mnX..mxX) {
                for (j in mnY..mxY) {
                    if (grid[i][j]) print('#')
                    else print('.')
                }
                println()
            }
            println("----------")
        }

        //printGrid()
        repeat(10) {
            simulateRound()
            val d = directions.removeFirst()
            directions.add(d)
           // printGrid()
        }

        var mnX = Integer.MAX_VALUE
        var mxX = Integer.MIN_VALUE
        var mnY = Integer.MAX_VALUE
        var mxY = Integer.MIN_VALUE
        for (i in grid.indices) for (j in grid[0].indices) {
            if (!grid[i][j]) continue
            mnX = minOf(mnX, i)
            mxX = maxOf(mxX, i)
            mnY = minOf(mnY, j)
            mxY = maxOf(mxY, j)
        }

        var ans = 0
        for (i in mnX..mxX) for (j in mnY..mxY)
            if (!grid[i][j])
                ans++

        return ans
    }

    fun part2(grid: Array<Array<Boolean>>): Int {
        val directions = mutableListOf(0, 1, 2, 3)

        fun simulateRound(): Boolean {

            val proposes = Array(grid.size) { Array(grid[0].size) { Pair(-1, -1) } }
            val proposedByCnt = Array(grid.size) { Array(grid[0].size) { 0 } }
            val newGrid = Array(grid.size) { Array(grid[0].size) { false } }

            fun checkNorth(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j-1] || grid[i-1][j] || grid[i-1][j+1]) return
                proposes[i][j] = Pair(i-1, j)
            }

            fun checkSouth(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i+1][j-1] || grid[i+1][j] || grid[i+1][j+1]) return
                proposes[i][j] = Pair(i+1, j)
            }

            fun checkWest(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j-1] || grid[i][j-1] || grid[i+1][j-1]) return
                proposes[i][j] = Pair(i, j-1)
            }

            fun checkEast(i: Int, j: Int) {
                if (proposes[i][j].first != -1) return
                if (grid[i-1][j+1] || grid[i][j+1] || grid[i+1][j+1]) return
                proposes[i][j] = Pair(i, j+1)
            }

            for (i in grid.indices) {
                for (j in grid[0].indices) {
                    if (i == 0 || j == 0 || i == grid.size-1 || j == grid[0].size-1)
                        continue
                    if (!grid[i][j]) continue

                    var cnt = 0
                    for (ni in (i-1)..(i+1)) for (nj in (j-1)..(j+1)) {
                        if (ni == i && nj == j) continue
                        if (ni < 0 || nj < 0 || ni >= grid.size || nj >= grid[0].size) continue
                        if (grid[ni][nj]) cnt++
                    }

                    if (cnt == 0) continue

                    for (dir in directions) {
                        when (dir) {
                            0 -> checkNorth(i, j)
                            1 -> checkSouth(i, j)
                            2 -> checkWest(i, j)
                            else -> checkEast(i, j)
                        }
                    }

                    if (proposes[i][j].first != -1) {
                        val p = proposes[i][j]
                        proposedByCnt[p.first][p.second]++
                    }
                }
            }

            var moved = false
            for (i in grid.indices) for (j in grid[0].indices) {
                if (!grid[i][j]) continue
                val p = proposes[i][j]
                if (p.first == -1) newGrid[i][j] = true
                else if (proposedByCnt[p.first][p.second] > 1) newGrid[i][j] = true
                else {
                    newGrid[p.first][p.second] = true
                    moved = true
                }
            }

            for (i in grid.indices) for (j in grid[0].indices)
                grid[i][j] = newGrid[i][j]

            return moved
        }

        fun printGrid() {
            println("--- Grid:")

            var mnX = Integer.MAX_VALUE
            var mxX = Integer.MIN_VALUE
            var mnY = Integer.MAX_VALUE
            var mxY = Integer.MIN_VALUE
            for (i in grid.indices) for (j in grid[0].indices) {
                if (!grid[i][j]) continue
                mnX = minOf(mnX, i)
                mxX = maxOf(mxX, i)
                mnY = minOf(mnY, j)
                mxY = maxOf(mxY, j)
            }

            var ans = 0
            for (i in mnX..mxX) {
                for (j in mnY..mxY) {
                    if (grid[i][j]) print('#')
                    else print('.')
                }
                println()
            }
            println("----------")
        }

        //printGrid()
        var round = 1
        while (simulateRound()){
            round++
//            simulateRound()
            val d = directions.removeFirst()
            directions.add(d)
            // printGrid()
        }

//        var mnX = Integer.MAX_VALUE
//        var mxX = Integer.MIN_VALUE
//        var mnY = Integer.MAX_VALUE
//        var mxY = Integer.MIN_VALUE
//        for (i in grid.indices) for (j in grid[0].indices) {
//            if (!grid[i][j]) continue
//            mnX = minOf(mnX, i)
//            mxX = maxOf(mxX, i)
//            mnY = minOf(mnY, j)
//            mxY = maxOf(mxY, j)
//        }

//        var ans = 0
//        for (i in mnX..mxX) for (j in mnY..mxY)
//            if (!grid[i][j])
//                ans++

        return round
    }



    fun parseGrid(lines: List<String>): Array<Array<Boolean>> {
        val grid = Array(1000) { Array(1000) { false } }
//        val grid = Array(lines.size) { Array(lines[0].length) { false } }

        val off = 500

        for (i in lines.indices) {
            for (j in lines[i].indices) {
                if (lines[i][j] == '#')
                    grid[i+off][j+off] = true
            }
        }

        return grid
    }





    val filename =
//        "inputs/day23_sample"
//        "inputs/day23_sample2"
        "inputs/day23"

    val input = readInput(filename)
    val grid1 = parseGrid(input)
    val grid2 = parseGrid(input)

    println("Part 1: ${part1(grid1)}")
    println("Part 2: ${part2(grid2)}")
}