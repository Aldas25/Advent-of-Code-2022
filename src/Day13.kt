fun main() {

    open class PacketList() {
        val list: MutableList<PacketList> = mutableListOf()

        override fun toString(): String {
            var res = "["
            for (p in list)
                res += "$p,"
            res += "]"
            return res
        }

        override fun equals(other: Any?): Boolean{
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as PacketList
            return this.list == other.list
        }
    }
    class PacketNumber(val number: Int) : PacketList() {
        override fun toString(): String {
            return number.toString()
        }

        override fun equals(other: Any?): Boolean{
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as PacketNumber
            return this.number == other.number
        }
    }

    fun comparePackets(left: PacketList, right: PacketList): Int {
        if (left == right) return 0
        if (left is PacketNumber && right is PacketNumber) {
            val numLeft = (left as PacketNumber).number
            val numRight = (right as PacketNumber).number
            return if (numLeft < numRight) -1
            else if (numLeft == numRight) 0
            else 1
        }
        if (left is PacketNumber) {
            val packet = PacketList()
            packet.list.add(left)
            return comparePackets(packet, right)
        }
        if (right is PacketNumber) {
            return comparePackets(right, left) * -1
        }

        // both left and right are packet lists
        val szLeft = left.list.size
        val szRight = right.list.size
        val size = minOf(szLeft, szRight)
        for (i in 0 until size) {
            val res = comparePackets(left.list[i], right.list[i])
            if (res == 0) continue
            return res
        }

        return if (szLeft < szRight) -1
        else if (szLeft == szRight) 0
        else 1
    }

    fun part1(pairs: List<Pair<PacketList, PacketList>>): Int {
        var ans = 0

        for (i in pairs.indices) {
            val pair = pairs[i]
            val res = comparePackets(pair.first, pair.second)
//            println(" comparing packets, res: $res")
            if (res < 1)
                ans += i+1
        }

        return ans
    }

    fun parseLine(line: String): PacketList {
        if (line[0] == '[') {
            val packet = PacketList()
            val inside = line.substring(1, line.length-1)
            if (inside.isNotEmpty()) {
                var newLine: String = ""
                var dep = 0
                for (c in inside) {
                    if (c == ',' && dep == 0) {
                        packet.list.add(parseLine(newLine))
                        newLine = ""
                        continue
                    }
                    if (c == '[') dep++
                    else if (c == ']') dep--
                    newLine += c
                }
                packet.list.add(parseLine(newLine))
            }
            return packet
        }

        return PacketNumber(line.toInt())
    }

    fun part2(packets: MutableList<PacketList>): Int {
        val divider1 = parseLine("[[2]]")
        val divider2 = parseLine("[[6]]")
        packets.add(divider1)
        packets.add(divider2)

        for (i in packets.indices) {
            var ch = i
            for (j in (i+1) until packets.size)
                if (comparePackets(packets[j], packets[ch]) == -1)
                    ch = j
            val tmp = packets[ch]
            packets[ch] = packets[i]
            packets[i] = tmp
        }

        var ans = 1

        for (i in packets.indices)
            if (packets[i] == divider1 || packets[i] == divider2)
                ans *= (i+1)

        return ans
    }

    fun parsePair(line1: String, line2: String): Pair<PacketList, PacketList> {
        return Pair(parseLine(line1), parseLine(line2))
    }

    fun parseInput(input: List<String>): List<Pair<PacketList, PacketList>> {
        var i = 0
        var list: List<Pair<PacketList, PacketList>> = emptyList()
        while (i < input.size) {
            list += parsePair(input[i], input[i+1])
            i += 3
        }
        return list
    }

    fun pairsToList(pairs: List<Pair<PacketList, PacketList>>): List<PacketList> {
        var list: List<PacketList> = emptyList()
        for (pair in pairs) {
            list += pair.first
            list += pair.second
        }
        return list
    }

    val filename =
//        "inputs/day13_sample"
        "inputs/day13"

    val input = readInput(filename)

    val pairs: List<Pair<PacketList, PacketList>> = parseInput(input)
    val list = pairsToList(pairs)

    println("Part 1: ${part1(pairs)}")
    println("Part 2: ${part2(list.toMutableList())}")
}