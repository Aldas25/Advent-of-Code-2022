fun main() {

    class Cube(val x: Int, val y: Int, val z: Int)

    val dirs = listOf(
        Cube(-1, 0, 0),
        Cube(1, 0, 0),
        Cube(0, -1, 0),
        Cube(0, 1, 0),
        Cube(0, 0, -1),
        Cube(0, 0, 1)
    )

    fun part1(cubes: List<Cube>): Int {
        var ans = 0

        val grid = Array(50) { Array(50) { Array(50) { false } } }

        for (cube in cubes) {
            if (grid[cube.x][cube.y][cube.z])
                println("repeating cubes: ${cube.x}, ${cube.y}, ${cube.z}")
            grid[cube.x][cube.y][cube.z] = true
        }

        for (cube in cubes) {
            for (dir in dirs) {
                val x = cube.x + dir.x
                val y = cube.y + dir.y
                val z = cube.z + dir.z
                if (x < 0 || y < 0 || z < 0 || !grid[x][y][z])
                    ans++
            }
        }

        return ans
    }

    fun part2(cubes: List<Cube>): Int {
        val grid = Array(50) { Array(50) { Array(50) { false } } }
        for (cube in cubes)
            grid[cube.x][cube.y][cube.z] = true

        val group = Array(50) { Array(50) { Array(50) { -1 } } }

        fun bfs(start: Cube, groupId: Int, isGrid: Boolean) {
            val q = mutableListOf(start)

            while (q.isNotEmpty()) {
                val cube = q.removeFirst()
                val x = cube.x
                val y = cube.y
                val z = cube.z
                if (x < 0 || y < 0 || z < 0) continue
                if (x >= 50 || y >= 50 || z >= 50) continue
                if (group[x][y][z] != -1) continue
                if (grid[x][y][z] != isGrid) continue

                group[x][y][z] = groupId
                for (dir in dirs) {
                    val nX = x + dir.x
                    val nY = y + dir.y
                    val nZ = z + dir.z
                    q.add(Cube(nX, nY, nZ))
                }
            }
        }

        var groupCnt = 0
        for (x in grid.indices)
            for (y in grid[0].indices)
                for (z in grid[0][0].indices)
                    if (group[x][y][z] == -1)
                        bfs(Cube(x, y, z), groupCnt++, grid[x][y][z])

        var ans = 0

        for (cube in cubes) {
            for (dir in dirs) {
                val x = cube.x + dir.x
                val y = cube.y + dir.y
                val z = cube.z + dir.z
                if (x < 0 || y < 0 || z < 0) {
                    ans++
                } else if (!grid[x][y][z] && group[x][y][z] == group[0][0][0])
                    ans++
            }
        }

        return ans
    }

    fun parseInput(input: List<String>): List<Cube> {
        fun parseCube(line: String): Cube {
            val parts = line.split(",")
            return Cube(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        }

        val list: MutableList<Cube> = mutableListOf()

        for (line in input)
            list.add(parseCube(line))

        return list
    }

    val filename =
//        "inputs/day18_sample"
        "inputs/day18"

    val input = readInput(filename)
    val cubes1 = parseInput(input)
    val cubes2 = parseInput(input)

    println("Part 1: ${part1(cubes1)}")
    println("Part 2: ${part2(cubes2)}")
}