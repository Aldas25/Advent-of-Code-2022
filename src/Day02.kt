fun main() {

    fun outcome(opponent: Int, me: Int): Int {
        if (opponent == me) {
            return 1
        }
        if ((opponent+1)%3 == me) {
            return 2
        }
        return 0
    }

    fun charToNum(c: Char): Int {
        if (c == 'A' || c == 'X')
            return 0
        if (c == 'B' || c == 'Y')
            return 1
        return 2
    }

    fun evalPermutation(permutation: List<Int>, input: List<String>): Int {
        var answer = 0

//        println("Evaluating $permutation")

        for (s in input) {
            val res = outcome(
                charToNum(s[0]),
                permutation[charToNum(s[2])]
            )

            answer += 3 * res + permutation[charToNum(s[2])] + 1

//            println("  current input: $s")
//            println("    current outcome: $res")
//            println("    perm: ${permutation[charToNum(s[2])]}")
        }

        return answer
    }

    fun part1(input: List<String>): Int {
        var answer = evalPermutation(listOf(0, 1, 2), input)

//        for (x in 0..2)
//            for (y in 0..2)
//                for (z in 0..2)
//                    if (x != y && y != z && z != x)
//                        answer = maxOf(
//                            answer,
//                            part1_evalScore(listOf(x, y, z), input)
//                        )

        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0

//        println("Evaluating $permutation")

        for (s in input) {
            val res = charToNum(s[2])
            val opponent = charToNum(s[0])

            val myChoice: Int
            if (res == 1) myChoice = opponent
            else if (res == 2) myChoice = (opponent+1)%3
            else myChoice = (opponent+2)%3

            answer += 3 * res + myChoice + 1

//            println("  current input: $s")
//            println("    current outcome: $res")
//            println("    myChoice: $myChoice")
        }

        return answer
    }

    val filename =
//        "inputs\\day02_sample"
        "inputs\\day02"

    val input = readInput(filename)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
