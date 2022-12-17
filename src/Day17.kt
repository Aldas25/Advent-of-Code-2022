import kotlin.math.max

fun main() {

    val rocks: List<Array<Array<Boolean>>> = listOf(
        // rock 1
        arrayOf(arrayOf(true, true, true, true)),

        // rock 2
        arrayOf(
            arrayOf(false, true, false),
            arrayOf(true, true, true),
            arrayOf(false, true, false)
        ),

        // rock 3
        arrayOf(
            arrayOf(true, true, true),
            arrayOf(false, false, true),
            arrayOf(false, false, true)
        ),

        // rock 4
        arrayOf(
            arrayOf(true),
            arrayOf(true),
            arrayOf(true),
            arrayOf(true)
        ),

        // rock 5
        arrayOf(
            arrayOf(true, true),
            arrayOf(true, true)
        )
    )

    fun part1(input: String): Int {
        val maxRocks = 2022

        var h = 0
        val grid = Array(100000) { Array(7) { false } }
        for (c in grid[0].indices)
            grid[0][c] = true

        var jet = 0

        fun canPlaceRock(rock: Array<Array<Boolean>>, pos: Pair<Int, Int>): Boolean {
            for (i in rock.indices)
                for (j in rock[0].indices) {
                    if (pos.first + i < 0 || pos.second + j < 0)
                        return false
                    if (pos.first + i >= grid.size || pos.second + j >= grid[0].size)
                        return false
                    if (rock[i][j] && grid[pos.first + i][pos.second + j])
                        return false
                }

            return true
        }

        fun dropRock(rock: Array<Array<Boolean>>) {
            var pos = Pair(h + 3 + 1, 2)

            fun jetPush() {
                var r = pos.first
                var c = pos.second
                if (input[jet] == '<') c--
                else c++
                jet = (jet + 1) % input.length

                if (canPlaceRock(rock, Pair(r, c)))
                    pos = Pair(r, c)
            }

            while (true) {
                jetPush()
                val r = pos.first - 1
                val c = pos.second
                if (!canPlaceRock(rock, Pair(r, c)))
                    break
                pos = Pair(r, c)
            }

            for (i in rock.indices)
                for (j in rock[0].indices) {
                    if (rock[i][j]) {
                        grid[pos.first + i][pos.second + j] = true
                        h = maxOf(h, pos.first + i)
                    }
                }
        }

        var rock = 0
        repeat(maxRocks) {
            dropRock(rocks[rock])
            rock = (rock + 1) % rocks.size
        }

        return h
    }

    fun part2(input: String): Long {
        val maxRocks = 1000000
        val answerRocks = 1000000000000L

        var h = 0
        val grid = Array(maxRocks * 5) { Array(7) { false } }
        for (c in grid[0].indices)
            grid[0][c] = true

        var jet = 0

        fun canPlaceRock(rock: Array<Array<Boolean>>, pos: Pair<Int, Int>): Boolean {
            for (i in rock.indices)
                for (j in rock[0].indices) {
                    if (pos.first + i < 0 || pos.second + j < 0)
                        return false
                    if (pos.first + i >= grid.size || pos.second + j >= grid[0].size)
                        return false
                    if (rock[i][j] && grid[pos.first + i][pos.second + j])
                        return false
                }

            return true
        }

        fun dropRock(rock: Array<Array<Boolean>>) {
            var pos = Pair(h + 3 + 1, 2)

            fun jetPush() {
                var r = pos.first
                var c = pos.second
                if (input[jet] == '<') c--
                else c++
                jet = (jet + 1) % input.length

                if (canPlaceRock(rock, Pair(r, c)))
                    pos = Pair(r, c)
            }

            while (true) {
                jetPush()
                val r = pos.first - 1
                val c = pos.second
                if (!canPlaceRock(rock, Pair(r, c)))
                    break
                pos = Pair(r, c)
            }

            for (i in rock.indices)
                for (j in rock[0].indices) {
                    if (rock[i][j]) {
                        grid[pos.first + i][pos.second + j] = true
                        h = maxOf(h, pos.first + i)
                    }
                }
        }

        var rock = 0
        val changes: MutableList<Long> = mutableListOf()
        repeat(maxRocks) {
            val lastH = h
            dropRock(rocks[rock])
            changes.add((h - lastH).toLong())
            rock = (rock + 1) % rocks.size
        }

        fun checkPattern(len: Int): Boolean {
            fun subEquals(fr1: Int, to1: Int, fr2: Int, to2: Int): Boolean {
                var i = 0
                while (fr1 + i <= to1) {
                    if (changes[fr1+i] != changes[fr2+i])
                        return false
                    i++
                }
                return true
            }

            val sz = changes.size
            // [sz-len; sz) should be similar to
            // [sz-2*len; sz-len)
            // [sz-i*len; sz-(i-1)*len)

            var i = 2
            while (true) {
                if (sz - (i+5)*len < 0) return true

                if(!subEquals(sz - i*len, sz - (i-1)*len - 1, sz - len, sz-1))
                    return false

                i++
            }

            return true
        }

        fun calcPattern(): Int {
            var i = 5
            while (!checkPattern(i)) {
                i++
            }
            return i
        }

        fun calcValueChanges(fr: Int, to: Int): Long {
            var res = 0L
            for (i in fr..to)
                res += changes[i]
            return res
        }

        val pattern = calcPattern().toLong()

        var ans = h.toLong()
        var movesLeft: Long = answerRocks - maxRocks
        ans += (movesLeft / pattern) * calcValueChanges(
            changes.size - pattern.toInt(),
            changes.size-1
        )
        movesLeft %= pattern.toInt()
        if (movesLeft > 0)
            ans += calcValueChanges(
                changes.size - pattern.toInt(),
                changes.size - pattern.toInt()+movesLeft.toInt()-1
            )

        return ans
    }

    val filename =
//        "inputs/day17_sample"
        "inputs/day17"

    val input = readInput(filename)

    println("Part 1: ${part1(input[0])}")
    println("Part 2: ${part2(input[0])}")
}