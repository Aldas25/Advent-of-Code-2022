import kotlin.math.abs

fun main() {

    class Node(val value: Long, var next: Node?, var prev: Node?)
    class CLL() {
//        lateinit var tail: Node
        val list: MutableList<Node> = mutableListOf()
        lateinit var node0: Node

        constructor(input: List<Long>) : this() {
            var prev: Node? = null
            for (x in input) {
                val node = Node(x, null, prev)
                list.add(node)
                if (prev != null) prev!!.next = node
                prev = node

                if (x == 0L)
                    node0 = node
            }
            prev!!.next = list[0]
            list[0].prev = prev
//            tail = prev
        }

        fun removeNode(node: Node) {
            node.prev!!.next = node.next
            node.next!!.prev = node.prev
        }

        fun getNext(node: Node, x: Long): Node {
            var finger = node
            var moves = 0
            while (moves < x) {
                moves++
                finger = finger.next!!
            }
            return finger
        }

        fun getPrev(node: Node, x: Long): Node {
            var finger = node
            var moves = 0
            while (moves < x) {
                moves++
                finger = finger.prev!!
            }
            return finger
        }

        fun insertAfter(node: Node, after: Node) {
            node.next = after.next
            node.prev = after
            after.next!!.prev = node
            after.next = node
        }

        fun moveNode_part1(node: Node) {
            val x = node.value
            if (x == 0L) return
            var aft: Node = node.next!!
            removeNode(node)
            if (x >= 0) aft = getNext(aft, x-1L)
            else aft = getPrev(aft, abs(x)+1L)
            insertAfter(node, aft)
        }

        fun print() {
            print(" Sequence: ")
            for (i in 0..6)
                print("${getNext(node0, i.toLong()).value} ")
            println()
        }

        fun doPart1() {
           // print()
            for (node in list) {
                moveNode_part1(node)
             //   print()
            }
        }

        fun calcPart1(): Long {
            return getNext(node0, 1000L).value +
                    getNext(node0, 2000L).value +
                    getNext(node0, 3000L).value
        }

        fun moveNode_part2(node: Node) {
            val x = node.value
            if (x == 0L) return

            var toMove = 0L

            if (x > 0) {
                toMove = (x-1L) % (list.size.toLong() - 1L)
            } else {
                toMove = (abs(x)+1L) % (list.size.toLong() - 1L)
            }

            if (toMove == 0L) return

            var aft: Node = node.next!!
            removeNode(node)
            if (x >= 0) aft = getNext(aft, toMove)
            else aft = getPrev(aft, toMove)
            insertAfter(node, aft)
        }

        fun doPart2() {
            // print()
            repeat(10) {
                for (node in list) {
                    moveNode_part2(node)
                    //   print()
                }
            }
        }

        fun calcPart2(): Long {
            return getNext(node0, 1000L).value +
                    getNext(node0, 2000L).value +
                    getNext(node0, 3000L).value
        }
    }

    fun part1(input: List<Int>): Int {
        val cll = CLL(input.map { x -> x.toLong() })
        cll.doPart1()
        return cll.calcPart1().toInt()
    }

    fun part2(input: List<Int>): Long {
        val list = input.map { x -> (x.toLong() * 811589153) }
        val cll = CLL(list)
        cll.doPart2()
        return cll.calcPart2()
    }

    val filename =
//        "inputs/day20_sample"
        "inputs/day20"

    val input = readInput(filename).map { x -> x.toInt() }

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}