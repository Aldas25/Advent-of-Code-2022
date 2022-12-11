fun main() {

    var g = 1L

    abstract class Operation {
        abstract fun doOperation(x: Long): Long
    }

    class Addition(val toAdd: Long): Operation() {
        override fun doOperation(x: Long): Long {
            return x + toAdd
        }
    }

    class Multiplication(val toMul: Long): Operation() {
        override fun doOperation(x: Long): Long {
            return x * toMul
        }
    }

    class Power(val pwr: Long): Operation() {
        override fun doOperation(x: Long): Long {
            var ans = 1L
            repeat(pwr.toInt()) {ans *= x}
            return ans
        }
    }

    class Monkey(val op: Operation, val testDivisible: Long, val throwTrue: Int, val throwFalse: Int) {
        val items = mutableListOf<Long>()
        var inspected = 0

        constructor(op: Operation, testDivisible: Long, throwTrue: Int, throwFalse: Int,
                    _items: List<Long>): this(op, testDivisible, throwTrue, throwFalse) {
            for (x in _items)
                items.add(x)
        }

        fun inspectPart1(): Pair<Int, Long> {
            if (items.isEmpty())
                return Pair(-1, -1)

            inspected++
            var x = items.removeFirst()
            x = op.doOperation(x)
            x /= 3
            if ((x % testDivisible) == 0L)
                return Pair(throwTrue, x)
            return Pair(throwFalse, x)
        }

        fun inspectPart2(): Pair<Int, Long> {
            if (items.isEmpty())
                return Pair(-1, -1)

            inspected++
            var x = items.removeFirst()
            x = op.doOperation(x)
            x %= g
            if ((x % testDivisible) == 0L)
                return Pair(throwTrue, x)
            return Pair(throwFalse, x)
        }
    }

    fun calculateG(monkeys: Array<Monkey>) {
        var cur = 1L
        for (monkey in monkeys)
            cur *= monkey.testDivisible
        g = cur
    }

    fun part1(monkeys: Array<Monkey>): Int {
        fun oneMonkey(monkey: Monkey) {
            while (true) {
                val p = monkey.inspectPart1()
                val throwTo = p.first
                val x = p.second
                if (throwTo == -1)
                    break
                monkeys[throwTo].items.add(x)
            }
        }

        fun round() {
            for (monkey in monkeys)
                oneMonkey(monkey)
        }

        fun printMonkeys() {
            println("after round: ")
            for (monkey in monkeys)
                println(" monkey has items ${monkey.items}")
            println()
        }

        repeat(20) {
            round()
//            printMonkeys()
        }

        val inspected = mutableListOf<Int>()
        for (monkey in monkeys)
            inspected.add(monkey.inspected)
//        println(" inspected: $inspected")
        inspected.sortDescending()
        val ans = inspected[0] * inspected[1]
        return ans
    }

    fun part2(monkeys: Array<Monkey>): Long {

        fun oneMonkey(monkey: Monkey) {
            while (true) {
                val p = monkey.inspectPart2()
                val throwTo = p.first
                val x = p.second
                if (throwTo == -1)
                    break
                monkeys[throwTo].items.add(x)
            }
        }

        fun round() {
            for (monkey in monkeys)
                oneMonkey(monkey)
        }

        fun printMonkeys(round: Int) {
            val inspected = mutableListOf<Long>()
            for (monkey in monkeys)
                inspected.add(monkey.inspected.toLong())
            println("after round $round: $inspected")
        }

        calculateG(monkeys)

        repeat(10000) {
            round()
//            if (it+1 in listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000))
//                printMonkeys(it)
        }

        val inspected = mutableListOf<Long>()
        for (monkey in monkeys)
            inspected.add(monkey.inspected.toLong())
//        println(" inspected: $inspected")
        inspected.sortDescending()
        val ans = inspected[0] * inspected[1]
        return ans

    }

    fun input(): Array<Monkey> {
        return arrayOf(
            Monkey(Multiplication(5), 2, 2, 1, listOf(50, 70, 89, 75, 66, 66)),
            Monkey(Power(2), 7, 3, 6, listOf(85)),
            Monkey(Addition(1), 13, 1, 3, listOf(66, 51, 71, 76, 58, 55, 58, 60)),
            Monkey(Addition(6), 3, 6, 4, listOf(79, 52, 55, 51)),
            Monkey(Multiplication(17), 19, 7, 5, listOf(69, 92)),
            Monkey(Addition(8), 5, 0, 2, listOf(71, 76, 73, 98, 67, 79, 99)),
            Monkey(Addition(7), 11, 7, 4, listOf(82, 76, 69, 69, 57)),
            Monkey(Addition(5), 17, 5, 0, listOf(65, 79, 86)),
        )
    }

    fun inputSample(): Array<Monkey> {
        return arrayOf(
            Monkey(Multiplication(19), 23, 2, 3, listOf(79, 98)),
            Monkey(Addition(6), 19, 2, 0, listOf(54, 65, 75, 74)),
            Monkey(Power(2), 13, 1, 3, listOf(79, 60, 97)),
            Monkey(Addition(3), 17, 0, 1, listOf(74)),
        )
    }

    val monkeys1: Array<Monkey> =
//        inputSample()
        input()

    val monkeys2: Array<Monkey> =
//        inputSample()
        input()

    println("Part 1: ${part1(monkeys1)}")
    println("Part 2: ${part2(monkeys2)}")
}