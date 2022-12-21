fun main() {

    abstract class Operation() {
        abstract fun doOperation(x: Long, y: Long): Long
        abstract fun getLeft(req: Long, y: Long): Long
        abstract fun getRight(req: Long, x: Long): Long
    }

    class AddOperation() : Operation() {
        override fun doOperation(x: Long, y: Long): Long {
            return x + y
        }

        override fun getLeft(req: Long, y: Long): Long {
            return req - y
        }

        override fun getRight(req: Long, x: Long): Long {
            return req - x
        }
    }

    class SubtractOperation() : Operation() {
        override fun doOperation(x: Long, y: Long): Long {
            return x - y
        }

        override fun getLeft(req: Long, y: Long): Long {
            return req + y
        }

        override fun getRight(req: Long, x: Long): Long {
            return x - req
        }
    }

    class MultiplyOperation() : Operation() {
        override fun doOperation(x: Long, y: Long): Long {
            return x * y
        }

        override fun getLeft(req: Long, y: Long): Long {
            return req / y
        }

        override fun getRight(req: Long, x: Long): Long {
            return req / x
        }
    }

    class DivideOperation() : Operation() {
        override fun doOperation(x: Long, y: Long): Long {
            return x / y
        }

        override fun getLeft(req: Long, y: Long): Long {
            return req * y
        }

        override fun getRight(req: Long, x: Long): Long {
            return x / req
        }
    }

    abstract class Node(val name: String) {
        var parent: Node? = null
        var onPath = false

        abstract fun getResult(): Long
        abstract fun solvePart2(reqNumber: Long): Long

        fun markPath() {
            onPath = true
            if (parent != null)
                parent!!.markPath()
        }
    }

    class OperationNode(name: String) : Node(name) {
        lateinit var left: Node
        lateinit var right: Node
        lateinit var op: Operation

        fun setValues(left: Node, right: Node, op: Operation) {
            this.left = left
            this.right = right
            this.op = op
        }

        override fun getResult(): Long {
            return op.doOperation(left.getResult(), right.getResult())
        }

        override fun solvePart2(reqNumber: Long): Long {
            if (name == "humn") return reqNumber
            if (name == "root") {
                if (!left.onPath) {
                    val req = left.getResult()
                    return right.solvePart2(req)
                }
                val req = right.getResult()
                return left.solvePart2(req)
            }

            if (!left.onPath) {
                val req = op.getRight(reqNumber, left.getResult())
                return right.solvePart2(req)
            }
            val req = op.getLeft(reqNumber, right.getResult())
            return left.solvePart2(req)
        }

    }

    class LeafNode(name: String) : Node(name) {
        var value = 0L

        override fun getResult(): Long {
            return value
        }

        override fun solvePart2(reqNumber: Long): Long {
            return reqNumber
        }

    }

    fun part1(nodes: List<Node>, nameToNode: Map<String, Node>): Long {
        return nameToNode["root"]!!.getResult()
    }

    fun part2(nodes: List<Node>, nameToNode: Map<String, Node>): Long {
        nameToNode["humn"]!!.markPath()
        return nameToNode["root"]!!.solvePart2(-1)
    }

    fun parseInput(input: List<String>): Pair<List<Node>, Map<String, Node>> {
        val list: MutableList<Node> = mutableListOf()
        val map: MutableMap<String, Node> = mutableMapOf()

        for (line in input) {
            val name = line.split(":")[0]
            lateinit var node: Node
            if (line.length > 15)
                node = OperationNode(name)
            else
                node = LeafNode(name)
            list.add(node)
            map[name] = node
        }

        fun getOperation(s: String): Operation {
            return when (s) {
                "+" -> AddOperation()
                "-" -> SubtractOperation()
                "*" -> MultiplyOperation()
                else -> DivideOperation()
            }
        }

        fun parseLine(line: String) {
            val parts = line.split(": ", " ")
            val name = parts[0]
            val node = map[name]!!
            if (node is LeafNode) {
                node as LeafNode
                node.value = parts[1].toLong()
            } else {
                node as OperationNode
                val left = map[parts[1]]!!
                val op = getOperation(parts[2])
                val right = map[parts[3]]!!
                left.parent = node
                right.parent = node
                node.setValues(left, right, op)
            }
        }

        for (line in input)
            parseLine(line)

        return Pair(list, map)
    }

    val filename =
//        "inputs/day21_sample"
        "inputs/day21"

    val input = readInput(filename)
    val pair1 = parseInput(input)
    val pair2 = parseInput(input)

    println("Part 1: ${part1(pair1.first, pair1.second)}")
    println("Part 2: ${part2(pair2.first, pair2.second)}")
}