import java.util.*
import kotlin.collections.HashMap

fun main() {

    class Node (val name: String) {
        val adjacent: MutableList<Node> = mutableListOf()
        var flow = 0
    }

    fun checkFlow(nodes: List<Node>, nameToNode: Map<String, Node>): Pair<List<Node>, Map<Node, Int>> {
        val nodeToId: MutableMap<Node, Int> = mutableMapOf()
        val flows: MutableList<Node> = mutableListOf()

        for (node in nodes) {
            if (node.flow > 0) {
                nodeToId[node] = flows.size
                flows.add(node)
            }
        }

        return Pair(flows, nodeToId)
    }

    fun part1(nodes: List<Node>, nameToNode: Map<String, Node>): Int {
        val maxTime = 30

        val checkedFlows = checkFlow(nodes, nameToNode)
        val flows = checkedFlows.first
        val flowNodeToId = checkedFlows.second

        fun calcMask(opened: List<Node>): Int {
            var result = 0
            for (node in opened) {
                result += 1 shl (flowNodeToId[node]!!)
            }
            return result
        }

        class State(val node: Node, val mask: Int, val time: Int) {
            public override fun hashCode(): Int {
                return Objects.hash(node.name, mask, time)
            }

            public override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || other !is State) return false
                other as State
                return this.node.name == other.node.name && this.mask == other.mask
                        && this.time == other.time
            }

            fun pressure(): Int {
                var res = 0
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        res += flows[i].flow
                return res
            }

            public override fun toString(): String {
                var str= " State - node: " + node.name
                str += ", time: $time, mask: "
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        str += flows[i].name + ", "
                return str
            }
        }

        fun getAdjacentStates(state: State): List<State> {
            val adj: MutableList<State> = mutableListOf()
            if (state.time >= maxTime)
                return adj

            adj.add(State(state.node, state.mask, state.time + 1))
            for (adjNode in state.node.adjacent)
                adj.add(State(adjNode, state.mask, state.time + 1))

            if (flowNodeToId.containsKey(state.node)) {
                if ((state.mask and (1 shl flowNodeToId[state.node]!!)) == 0) {
                    adj.add(
                        State(
                            state.node,
                            state.mask + (1 shl (flowNodeToId[state.node]!!)),
                            state.time + 1
                        )
                    )
                }
            }

            return adj
        }

        val dp: HashMap<State, Int> = hashMapOf()
//        val visited: HashSet<State> = hashSetOf()

        fun printStateInfo(state: State) {
            println("State $state\n  dp = ${dp[state]}")
        }

//        val debugStates = listOf(
//
//            State(nameToNode["AA"]!!, calcMask(listOf()), 1),
//            State(nameToNode["DD"]!!, calcMask(listOf()), 2),
//            State(nameToNode["DD"]!!, calcMask(listOf(
//                nameToNode["DD"]!!
//            )), 3),
//
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 24),
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 25),
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["CC"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 26)
//        )
        val debugStates: List<State> = emptyList()

        fun dfs(state: State): Int {
            if (dp.containsKey(state)) return dp[state]!!
//            if (visited.contains(state)) return 0
//            visited.add(state)

            val adjStates = getAdjacentStates(state)
            dp[state] = 0

            var chosenState = state
            for (adjState in adjStates) {
                dp[state] = maxOf(dp[state]!!, dfs(adjState))
                if (dfs(adjState) == dp[state]!!)
                    chosenState = adjState
            }

            dp[state] = dp[state]!! + state.pressure()

            if (state in debugStates) {
                printStateInfo(state)
                print("  next state:  ")
                printStateInfo(chosenState)
            }

            return dp[state]!!
        }

        val ans = dfs(State(nameToNode["AA"]!!, 0, 1))

//        printStateInfo(State(nameToNode["CC"]!!, calcMask(listOf(
//            nameToNode["BB"]!!, nameToNode["CC"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//            nameToNode["HH"]!!, nameToNode["JJ"]!!
//        )), 25))

//        printStateInfo(State(nameToNode["CC"]!!, calcMask(listOf(
//            nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//            nameToNode["HH"]!!, nameToNode["JJ"]!!
//        )), 24))



        return ans
    }

    fun part2(nodes: List<Node>, nameToNode: Map<String, Node>): Int {
        val maxTime = 26

        val checkedFlows = checkFlow(nodes, nameToNode)
        val flows = checkedFlows.first
        val flowNodeToId = checkedFlows.second

        fun calcMask(opened: List<Node>): Int {
            var result = 0
            for (node in opened) {
                result += 1 shl (flowNodeToId[node]!!)
            }
            return result
        }

        class State(val node: Node, val mask: Int, val time: Int) {
            public override fun hashCode(): Int {
                return Objects.hash(node.name, mask, time)
            }

            public override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || other !is State) return false
                other as State
                return this.node.name == other.node.name && this.mask == other.mask
                        && this.time == other.time
            }

            fun pressure(): Int {
                var res = 0
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        res += flows[i].flow
                return res
            }

            public override fun toString(): String {
                var str= " State - node: " + node.name
                str += ", time: $time, mask: "
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        str += flows[i].name + ", "
                return str
            }
        }

        fun getAdjacentStates(state: State): List<State> {
            val adj: MutableList<State> = mutableListOf()
            if (state.time >= maxTime)
                return adj

            adj.add(State(state.node, state.mask, state.time + 1))
            for (adjNode in state.node.adjacent)
                adj.add(State(adjNode, state.mask, state.time + 1))

            if (flowNodeToId.containsKey(state.node)) {
                if ((state.mask and (1 shl flowNodeToId[state.node]!!)) == 0) {
                    adj.add(
                        State(
                            state.node,
                            state.mask + (1 shl (flowNodeToId[state.node]!!)),
                            state.time + 1
                        )
                    )
                }
            }

            return adj
        }

        val dp: HashMap<State, Int> = hashMapOf()
        val bestValForMask: MutableMap<Int, Int> = mutableMapOf()
//        val visited: HashSet<State> = hashSetOf()

        fun printStateInfo(state: State) {
            println("State $state\n  dp = ${dp[state]}")
        }

//        val debugStates = listOf(
//
//            State(nameToNode["AA"]!!, calcMask(listOf()), 1),
//            State(nameToNode["DD"]!!, calcMask(listOf()), 2),
//            State(nameToNode["DD"]!!, calcMask(listOf(
//                nameToNode["DD"]!!
//            )), 3),
//
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 24),
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 25),
//            State(nameToNode["CC"]!!, calcMask(listOf(
//                nameToNode["BB"]!!, nameToNode["CC"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//                nameToNode["HH"]!!, nameToNode["JJ"]!!
//            )), 26)
//        )
        val debugStates: List<State> = emptyList()

        fun bfs(start: State) {
//            if (dp.containsKey(state)) return dp[state]!!
//            if (visited.contains(state)) return 0
//            visited.add(state)

            val pq: PriorityQueue<Pair<Int, State>> = PriorityQueue<Pair<Int, State>> {
                p1, p2 -> Integer.signum(p1.second.time - p2.second.time)
            }

            pq.add(Pair(0, start))
            dp[start] = 0

            val vis: MutableMap<State, Boolean> = mutableMapOf()

            while (pq.isNotEmpty()) {
                val state = pq.poll().second

                if (vis.containsKey(state) && vis[state]!!) continue
                vis[state] = true

                dp[state] = dp[state]!! + state.pressure()

                if (!bestValForMask.containsKey(state.mask))
                    bestValForMask[state.mask] = 0

                bestValForMask[state.mask] = maxOf(
                    bestValForMask[state.mask]!!,
                    dp[state]!!
                )

                val adjStates = getAdjacentStates(state)

//                var chosenState = state
                for (adjState in adjStates) {
                    if (!dp.containsKey(adjState) || dp[adjState]!! < dp[state]!!) {
                        dp[adjState] = dp[state]!!
                        pq.add(Pair(dp[adjState]!!, adjState))
                    }
//                    if (dfs(adjState) == dp[state]!!)
//                        chosenState = adjState
                }
            }
        }

        bfs(State(nameToNode["AA"]!!, 0, 1))

        val masks: MutableList<Int> = mutableListOf()
        for (key in bestValForMask.keys)
            masks.add(key)

//        val myMask = calcMask(listOf(
//            nameToNode["JJ"]!!, nameToNode["BB"]!!, nameToNode["CC"]!!
//        ))
//        val elMask = calcMask(listOf(
//            nameToNode["DD"]!!, nameToNode["HH"]!!, nameToNode["EE"]!!
//        ))
//        println(" should be: $myMask, $elMask, ${
//            bestValForMask[myMask]!! + bestValForMask[elMask]!!
//        }"
//                + " = ${bestValForMask[myMask]!!} + ${bestValForMask[elMask]!!}")


        var ans = 0
        for (id1 in masks.indices)
            for (id2 in (id1+1) until masks.size) {
                val mask1 = masks[id1]
                val mask2 = masks[id2]
                if ((mask1 and mask2) != 0)
                    continue
                val cur = (bestValForMask[mask1]!!) + (bestValForMask[mask2]!!)
//                println("cur: $mask1, $mask2, $cur"
//                        + " = ${bestValForMask[mask1]!!} + ${bestValForMask[mask2]!!}")
                if (cur > ans) {
                    ans = cur
//                    println(" new best: $mask1, $mask2, $cur"
//                    + " = ${bestValForMask[mask1]!!} + ${bestValForMask[mask2]!!}")
                }
            }

//        printStateInfo(State(nameToNode["CC"]!!, calcMask(listOf(
//            nameToNode["BB"]!!, nameToNode["CC"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//            nameToNode["HH"]!!, nameToNode["JJ"]!!
//        )), 25))

//        printStateInfo(State(nameToNode["CC"]!!, calcMask(listOf(
//            nameToNode["BB"]!!, nameToNode["DD"]!!, nameToNode["EE"]!!,
//            nameToNode["HH"]!!, nameToNode["JJ"]!!
//        )), 24))



        return ans
    }

/*
    fun part2(nodes: List<Node>, nameToNode: Map<String, Node>): Int {
        val maxTime = 26

        val checkedFlows = checkFlow(nodes, nameToNode)
        val flows = checkedFlows.first
        val flowNodeToId = checkedFlows.second

        fun calcMask(opened: List<Node>): Int {
            var result = 0
            for (node in opened) {
                result += 1 shl (flowNodeToId[node]!!)
            }
            return result
        }

        class State(val node: Node, val elephant: Node, val mask: Int, val time: Int) {
            public override fun hashCode(): Int {
                return Objects.hash(node.name, elephant.name, mask, time)
            }

            public override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || other !is State) return false
                other as State
                return this.node.name == other.node.name
                        && this.elephant.name == other.elephant.name
                        && this.mask == other.mask
                        && this.time == other.time
            }

            fun pressure(): Int {
                var res = 0
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        res += flows[i].flow
                return res
            }

            public override fun toString(): String {
                var str = " State - node: " + node.name
                str += ",  elephant " + elephant.name
                str += ", time: $time, mask: "
                for (i in flows.indices)
                    if ((mask and (1 shl i)) != 0)
                        str += flows[i].name + ", "
                return str
            }
        }

        fun getAdjacentStates(state: State): List<State> {
            val adj: MutableList<State> = mutableListOf()
            if (state.time >= maxTime)
                return adj

//            adj.add(State(state.node, state.elephant, state.mask, state.time + 1))
            var checkListNode = state.node.adjacent
            var checkListElephant = state.elephant.adjacent
            if (checkListNode.isEmpty())
                checkListNode = mutableListOf(state.node)
            if (checkListElephant.isEmpty())
                checkListElephant = mutableListOf(state.elephant)

//            for (adjNode in state.node.adjacent)
//                adj.add(State(adjNode, state.elephant, state.mask, state.time + 1))
//            for (adjNodeElephant in state.elephant.adjacent)
//                adj.add(State(state.node, adjNodeElephant, state.mask, state.time + 1))

            for (adjNode in checkListNode)
                for (adjNodeElephant in checkListElephant)
                    adj.add(State(adjNode, adjNodeElephant, state.mask, state.time + 1))

            if (flowNodeToId.containsKey(state.node)) {
                if ((state.mask and (1 shl flowNodeToId[state.node]!!)) == 0) {
//                    adj.add(
//                        State(
//                            state.node,
//                            state.elephant,
//                            state.mask + (1 shl (flowNodeToId[state.node]!!)),
//                            state.time + 1
//                        )
//                    )

                    for (adjNodeElephant in checkListElephant)
                        adj.add(
                            State(
                                state.node, adjNodeElephant,
                                state.mask + (1 shl (flowNodeToId[state.node]!!)),
                                state.time + 1
                            )
                        )
                }
            }

            if (flowNodeToId.containsKey(state.elephant)) {
                if ((state.mask and (1 shl flowNodeToId[state.elephant]!!)) == 0) {
//                    adj.add(
//                        State(
//                            state.node,
//                            state.elephant,
//                            state.mask + (1 shl (flowNodeToId[state.elephant]!!)),
//                            state.time + 1
//                        )
//                    )

                    for (adjNode in checkListNode)
                        adj.add(
                            State(
                                adjNode, state.elephant,
                                state.mask + (1 shl (flowNodeToId[state.elephant]!!)),
                                state.time + 1
                            )
                        )
                }
            }

            if (flowNodeToId.containsKey(state.node)) {
                if (flowNodeToId.containsKey(state.elephant)) {
                    if ((state.mask and (1 shl flowNodeToId[state.node]!!)) == 0) {
                        if ((state.mask and (1 shl flowNodeToId[state.elephant]!!)) == 0) {
                            if (state.elephant.name != state.node.name) {
                                adj.add(
                                    State(
                                        state.node,
                                        state.elephant,
                                        state.mask + (1 shl (flowNodeToId[state.node]!!))
                                        + (1 shl (flowNodeToId[state.elephant]!!)),
                                        state.time + 1
                                    )
                                )
                            }
                        }
                    }
                }
            }

            return adj
        }

        val dp: HashMap<State, Int> = hashMapOf()
//        val visited: HashSet<State> = hashSetOf()

        fun printStateInfo(state: State) {
            println("State $state\n  dp = ${dp[state]}")
        }

//        val debugStates = listOf(
//
//            State(nameToNode["AA"]!!,nameToNode["AA"]!!, calcMask(listOf()), 1),
//            State(nameToNode["II"]!!,nameToNode["DD"]!!, calcMask(listOf()), 2),
//            State(nameToNode["JJ"]!!,nameToNode["DD"]!!, calcMask(listOf(
//                nameToNode["DD"]!!
//            )), 3),
//            State(nameToNode["JJ"]!!,nameToNode["EE"]!!, calcMask(listOf(
//                nameToNode["DD"]!!, nameToNode["JJ"]!!
//            )), 4),
//        )
//        val debugStates: List<State> = emptyList()

        var stateCalc = 0

        fun dfs(state: State): Int {
            if (dp.containsKey(state)) return dp[state]!!
//            if (visited.contains(state)) return 0
//            visited.add(state)

            val adjStates = getAdjacentStates(state)
            dp[state] = 0

//            var chosenState = state
            for (adjState in adjStates) {
                dp[state] = maxOf(dp[state]!!, dfs(adjState))
//                if (dfs(adjState) == dp[state]!!)
//                    chosenState = adjState
            }

            dp[state] = dp[state]!! + state.pressure()

//            if (state in debugStates) {
//                printStateInfo(state)
//                print("  next state:  ")
//                printStateInfo(chosenState)
//            }

            stateCalc++
            if (stateCalc % 1000000 == 0)
                println("stateCalc: $stateCalc")

            return dp[state]!!
        }

        return dfs(State(nameToNode["AA"]!!, nameToNode["AA"]!!, 0, 1))
    }
 */

    fun parseInput(input: List<String>): Pair<List<Node>, Map<String, Node>> {
        val nameToNode: MutableMap<String, Node> = mutableMapOf()
        val nodes: MutableList<Node> = mutableListOf()

        fun node(name: String): Node {
            if (!nameToNode.containsKey(name)) {
                val newNode = Node(name)
                nodes.add(newNode)
                nameToNode[name] = newNode
            }
            return nameToNode[name]!!
        }

        fun parseLine(line: String) {
            val parts = line.split(
                "Valve ", " has flow rate=", "; tunnels lead to valves ", "; tunnel leads to valve "
            )
            val currentNode = node(parts[1])
            val flow = parts[2].toInt()
            currentNode.flow = flow
            val adj = parts[3].split(", ")
            for (a in adj)
                currentNode.adjacent.add(node(a))
        }

        for (line in input)
            parseLine(line)

        return Pair(nodes, nameToNode)
    }

    val filename =
//        "inputs/day16_sample"
        "inputs/day16"

    val input = readInput(filename)
    val nodes1 = parseInput(input)
    val nodes2 = parseInput(input)

    println("Part 1: ${part1(nodes1.first, nodes1.second)}")
    println("Part 2: ${part2(nodes2.first, nodes2.second)}")
}