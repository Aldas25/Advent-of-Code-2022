fun main() {

    var part = 1
    val connected: MutableMap<Pair<Int, Int>, Pair<Int, Int>> = mutableMapOf()
    val conDir: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    fun posOff(newPos: Pair<Int, Int>, grid: Array<Array<Int>>): Boolean {
        return newPos.first < 0 || newPos.first >= grid.size || newPos.second < 0 ||
                newPos.second >= grid[0].size || grid[newPos.first][newPos.second] == 0
    }

    fun step1(pos: Pair<Int, Int>, grid: Array<Array<Int>>, dx: Int, dy: Int): Pair<Int, Int> {
        var newPos = Pair(pos.first + dx, pos.second + dy)
        if (posOff(newPos, grid)) {
            newPos = pos
            while (!posOff(newPos, grid)) {
                newPos = Pair(newPos.first - dx, newPos.second - dy)
            }
            newPos = Pair(newPos.first + dx, newPos.second + dy)
        }
        if (grid[newPos.first][newPos.second] == 2) return pos
        return newPos
    }

    fun step2(pos: Pair<Int, Int>, grid: Array<Array<Int>>, dx: Int, dy: Int, dir: Int): Pair<Pair<Int, Int>, Int> {
        var newPos = Pair(pos.first + dx, pos.second + dy)
        var newDir = dir
        if (posOff(newPos, grid)) {
            newPos = connected[pos]!!
            newDir = conDir[pos]!!
        }
        if (grid[newPos.first][newPos.second] == 2) return Pair(pos, dir)
        return Pair(newPos, newDir)
    }

    val dxs = listOf(0, 1, 0, -1)
    val dys = listOf(1, 0, -1, 0)

    fun doMove1(move: String, pos: Pair<Int, Int>, dir: Int, grid: Array<Array<Int>>): Pair<Pair<Int, Int>, Int> {
        if (move == "L") {
            val newDir = (dir - 1 + 4) % 4
            return Pair(pos, newDir)
        }
        if (move == "R") {
            val newDir = (dir + 1) % 4
            return Pair(pos, newDir)
        }
        val steps = move.toInt()
        var newPos = pos
        repeat(steps) {
            newPos = step1(newPos, grid, dxs[dir], dys[dir])
        }

        return Pair(newPos, dir)
    }

    fun doMove2(move: String, pos: Pair<Int, Int>, dir: Int, grid: Array<Array<Int>>): Pair<Pair<Int, Int>, Int> {
        if (move == "L") {
            val newDir = (dir - 1 + 4) % 4
            return Pair(pos, newDir)
        }
        if (move == "R") {
            val newDir = (dir + 1) % 4
            return Pair(pos, newDir)
        }
        val steps = move.toInt()
        var newPos = pos
        var newDir = dir
        repeat(steps) {
            val p = step2(newPos, grid, dxs[newDir], dys[newDir], newDir)
            newPos = p.first
            newDir = p.second
        }

        return Pair(newPos, newDir)
    }

    fun part1(grid: Array<Array<Int>>, path: List<String>): Long {
        var pos = Pair(0, 0)
        while (grid[pos.first][pos.second] == 0) {
            pos = Pair(pos.first, pos.second+1)
        } // found initial pos

        var dir = 0 // right

        for (move in path) {
            val p = doMove1(move, pos, dir, grid)
            pos = p.first
            dir = p.second
        }

        return 1000 * (pos.first.toLong() + 1) + 4 * (pos.second.toLong() + 1) + dir.toLong()
    }

    fun part2(grid: Array<Array<Int>>, path: List<String>): Long {
        part = 2
        var pos = Pair(0, 0)
        while (grid[pos.first][pos.second] == 0) {
            pos = Pair(pos.first, pos.second+1)
        } // found initial pos

        var dir = 0 // right

        for (move in path) {
            val p = doMove2(move, pos, dir, grid)
            pos = p.first
            dir = p.second
        }

        return 1000 * (pos.first.toLong() + 1) + 4 * (pos.second.toLong() + 1) + dir.toLong()
    }

    fun parseInput(input: List<String>): Pair<Array<Array<Int>>, List<String>> {
        fun splitPath(line: String): List<String> {
            val path: MutableList<String> = mutableListOf()
            var cur = ""
            for (c in line) {
                if (c == 'L' || c == 'R') {
                    path.add(cur)
                    path.add(c.toString())
                    cur = ""
                } else cur += c
            }
            path.add(cur)
            return path
        }

        fun parseGrid(lines: List<String>): Array<Array<Int>> {
            val rows = lines.size
            var columns = 0
            for (line in lines)
                columns = maxOf(columns, line.length)
            val grid = Array(rows) { Array(columns) {0} }

            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    if (lines[r].length <= c)
                        grid[r][c] = 0
                    else if (lines[r][c] == ' ')
                        grid[r][c] = 0
                    else if (lines[r][c] == '.')
                        grid[r][c] = 1
                    else
                        grid[r][c] = 2
                }
            }

            return grid
        }

        val gridLines = input.subList(0, input.size-2)
        val pathLine = input[input.size-1]

        val grid = parseGrid(gridLines)
        val path = splitPath(pathLine)
        return Pair(grid, path)
    }

    fun connect(a: Pair<Int, Int>, b: Pair<Int, Int>, dirA: Int, dirB: Int) {
        connected[a] = b
        connected[b] = a

        conDir[a] = dirA
        conDir[b] = dirB
    }

    fun makeConnectedSample() {
        // 1-3
        for (r in 0..3) {
            connect(Pair(r, 8), Pair(4, 4 + r), 1, 0)
        }

        // 3-5
        for (r in 8..11) {
            connect(Pair(r, 8), Pair(7, 7 - r + 8), 0, 3)
        }

        // 4-6
        for (r in 4..7) {
            connect(Pair(r, 11), Pair(8, 15 - r + 4), 1, 2)
        }

        // 5-2
        for (c in 8..11) {
            connect(Pair(11, c), Pair(7, 3 - c + 8), 3, 3)
        }

        // not all here, but not all needed in sample
    }

    fun makeConnected() {
        // 2-3
        for (c in 100..149) {
            connect(Pair(49, c), Pair(50 + c - 100, 99), 2, 3)
        }

        // 3-5
        for (r in 50..99) {
            connect(Pair(r, 50), Pair(100, 0 + r - 50), 1, 0)
        }

        // 4-6
        for (c in 50..99) {
            connect(Pair(149, c), Pair(150 + c - 50, 49), 2, 3)
        }

        // 1-5
        for (r in 0..49) {
            connect(Pair(r, 50), Pair(149 - r + 0, 0), 0, 0)
        }

        // 2-4
        for (r in 0..49) {
            connect(Pair(r, 149), Pair(149 - r + 0, 99), 2, 2)
        }

        // 1-6
        for (c in 50..99) {
            connect(Pair(0, c), Pair(150+c-50, 0), 0, 1)
        }

        // 2-6
        for (c in 100..149) {
            connect(Pair(0, c), Pair(199, 0+c-100), 3, 1)
        }
    }

    val filename =
//        "inputs/day22_sample"
        "inputs/day22"
//    makeConnectedSample()
    makeConnected()

    val input = readInput(filename)
    val pair1 = parseInput(input)
    val pair2 = parseInput(input)

    println("Part 1: ${part1(pair1.first, pair1.second)}")
    println("Part 2: ${part2(pair2.first, pair2.second)}")
}