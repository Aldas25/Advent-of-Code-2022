fun main() {

    fun priority(c: Char): Int {
        if (c in 'a'..'z')
            return (c - 'a' + 1)
        return (c - 'A' + 27)
    }

    fun part1(input: List<String>): Int {
        var ans = 0

        for (s in input) {
            var firstHalf: Set<Char> = emptySet<Char>()
            val sz = s.length
            for (i in 0..(sz/2-1)){
                firstHalf += s[i]
            }

            var foundChars = emptySet<Char>()

            for (i in (sz/2)..(sz-1)) {
                if (s[i] in firstHalf) {
                    foundChars += s[i]
                }
            }

            for (c in foundChars)
                ans += priority(c)
        }

        return ans
    }

    fun part2(input: List<String>): Int {
        var ans = 0

        for (i in 0..(input.size-1)) {
            if (i%3 != 0) continue

            val badge: Set<Char> = input[i].toSet() intersect input[i+1].toSet() intersect input[i+2].toSet()
            for (c in badge)
                ans += priority(c)
        }

        return ans
    }

    val filename =
//        "inputs\\day03_sample"
        "inputs\\day03"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
