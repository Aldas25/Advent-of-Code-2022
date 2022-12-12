fun main() {

    fun elevation(i: Int, j: Int, grid: List<String>): Int {
        val c = grid[i][j]
        if (c in 'a'..'z')
            return c - 'a'
        if (c == 'S')
            return 'a' - 'a'
        // else c == 'E'
        return 'z' - 'a'
    }

    fun part1(grid: List<String>): Int {

        val r = grid.size
        val c = grid[0].length

//        val visited = Array(r) {Array(c) { false } }
        val dist = Array(r) {Array(c) { -1 } }

        val queue = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until r)
            for (j in 0 until c)
                if (grid[i][j] == 'S') {
                    queue.add(Pair(i, j))
                    dist[i][j] = 0
                }

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            val i = cur.first
            val j = cur.second

            if (grid[i][j] == 'E')
                return dist[i][j]

            for (adj in listOf(Pair(i-1, j), Pair(i+1, j), Pair(i, j-1), Pair(i, j+1))) {
                val ni = adj.first
                val nj = adj.second
                if (ni < 0 || nj < 0 || ni >= r || nj >= c)
                    continue
                if (elevation(ni, nj, grid) - elevation(i, j, grid) > 1) continue
                if (dist[ni][nj] == -1) {
                    dist[ni][nj] = dist[i][j] + 1
                    queue.add(adj)
                }
            }
        }

        return -1
    }

    fun part2(grid: List<String>): Int {

        val r = grid.size
        val c = grid[0].length

//        val visited = Array(r) {Array(c) { false } }
        val dist = Array(r) {Array(c) { -1 } }

        val queue = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until r)
            for (j in 0 until c)
                if (grid[i][j] == 'E') {
                    queue.add(Pair(i, j))
                    dist[i][j] = 0
                }

        var ans = Integer.MAX_VALUE

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            val i = cur.first
            val j = cur.second

            if (elevation(i, j, grid) == 'a' - 'a')
                ans = minOf(ans, dist[i][j])

            for (adj in listOf(Pair(i-1, j), Pair(i+1, j), Pair(i, j-1), Pair(i, j+1))) {
                val ni = adj.first
                val nj = adj.second
                if (ni < 0 || nj < 0 || ni >= r || nj >= c)
                    continue
                if (elevation(ni, nj, grid) - elevation(i, j, grid) < -1) continue
                if (dist[ni][nj] == -1) {
                    dist[ni][nj] = dist[i][j] + 1
                    queue.add(adj)
                }
            }
        }

        return ans
    }



    val filename =
//        "inputs/day12_sample"
        "inputs/day12"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}