// for part 1
val sizeLimit = 100000

// for part 2
val totalSpace = 70000000
val needUnused = 30000000

fun main() {

    fun part1(root: Node): Int {
        return root.part1()
    }

    fun part2(root: Node): Int {
        val removeAtLeast = needUnused - (totalSpace - root.size)
        return root.part2(removeAtLeast)
    }

    fun constructTree(input: List<String>): Node {

        val root = Node("/",false, null)
        var current = root

        for (i in input.indices) {
            val s = input[i]
            if (s[0] != '$') continue

            //println("now i=$i, current name = ${current.name}")

            if (s[2] == 'c') {
                // cd
                val directory = s.substring(5)
                if (directory == "..")
                    current = current.parent!!
                else if (directory == "/")
                    current = root
                else
                    for (child in current.children) {
                        if (child.name == directory) {
                            current = child
                            break
                        }
                    }
            } else {
                // ls
                var j = i+1
                while (j < input.size && input[j][0] != '$') {
                    val line = input[j]
                    val parts = line.split(" ")
                    val name = parts[1]
                    val child = if (parts[0] == "dir") {
                        // dir
                        Node(name, false, current)
                    } else {
                        // file
                        val size = parts[0].toInt()
                        Node(name, true, current, size)
                    }
                    current.addChild(child)

                    j++
                }
            }

        }

        return root
    }

    val filename =
//        "inputs/day07_sample"
        "inputs/day07"

    val input = readInput(filename)
    val root = constructTree(input)
    root.calcSizes()

    println("Part 1: ${part1(root)}")
    println("Part 2: ${part2(root)}")
}

class Node(val name: String, val isLeaf: Boolean, val parent: Node?) {

    var size: Int = 0

    constructor(name: String, isLeaf: Boolean, parent: Node?, size: Int) : this(name, isLeaf, parent) {
        this.size = size
    }

    val children = mutableListOf<Node>()

    fun addChild(child: Node) {
        children.add(child)
    }

    fun calcSizes() {
        if (isLeaf) return

        size = 0
        for (child in children) {
            child.calcSizes()
            size += child.size
        }
    }

    fun part1(): Int {
        if (isLeaf)
            return 0

        var ans = 0
        if (size <= sizeLimit) {
            ans += size
        }

        for (child in children) {
            ans += child.part1()
        }

        return ans
    }

    fun part2(removeAtLeast: Int): Int {
        if (isLeaf) return Int.MAX_VALUE

        var ans = Int.MAX_VALUE
        if (size >= removeAtLeast) ans = minOf(ans, size)
        for (child in children)
            ans = minOf(ans, child.part2(removeAtLeast))

        return ans
    }

}