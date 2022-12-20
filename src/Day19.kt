import kotlin.math.max

fun main() {

    fun toId(resource: String): Int {
        if (resource == "ore") return 0
        if (resource == "clay") return 1
        if (resource == "obsidian") return 2
        return 3
    }

    val part1_minutes = 24
    var maxMin = 0
    var maxOreNeeded = 0
    var maxClayNeeded = 0
    var maxObsidianNeeded = 0
    var maxGeodes = 0

    class Blueprint(
        val id: Int, val oreOre: Int, val clayOre: Int,
        val obsidianOre: Int, val obsidianClay: Int,
        val geodeOre: Int, val geodeObsidian: Int
    )

    var curBlueprint: Blueprint = Blueprint(0, 0, 0, 0, 0, 0, 0)

    class State(
        val time: Int,
        val resOre: Int, val resClay: Int, val resObsidian: Int, val resGeode: Int,
        val robOre: Int, val robClay: Int, val robObsidian: Int
    ) {

        fun mineAndNotBuy(): State {
            return State(
                time+1,
                resOre + robOre, resClay + robClay,
                resObsidian + robObsidian, resGeode,
                robOre, robClay, robObsidian
            )
        }

        fun mineAndBuyOre(): State {
            return State(
                time+1,
                resOre + robOre - curBlueprint.oreOre, resClay + robClay,
                resObsidian + robObsidian, resGeode,
                robOre + 1, robClay, robObsidian
            )
        }

        fun mineAndBuyClay(): State {
            return State(
                time+1,
                resOre + robOre - curBlueprint.clayOre, resClay + robClay,
                resObsidian + robObsidian, resGeode,
                robOre, robClay + 1, robObsidian
            )
        }

        fun mineAndBuyObsidian(): State {
            return State(
                time+1,
                resOre + robOre - curBlueprint.obsidianOre,
                resClay + robClay - curBlueprint.obsidianClay,
                resObsidian + robObsidian, resGeode,
                robOre, robClay, robObsidian + 1
            )
        }

        fun mineAndBuyGeode(): State {
            return State(
                time+1,
                resOre + robOre - curBlueprint.geodeOre, resClay + robClay,
                resObsidian + robObsidian - curBlueprint.geodeObsidian,
                resGeode + (maxMin - time - 1),
                robOre, robClay, robObsidian
            )
        }

        fun canBuyOre(): Boolean {
            return resOre >= curBlueprint.oreOre
        }

        fun canBuyClay(): Boolean {
            return resOre >= curBlueprint.clayOre
        }

        fun canBuyObsidian(): Boolean {
            return resOre >= curBlueprint.obsidianOre && resClay >= curBlueprint.obsidianClay
        }

        fun canBuyGeode(): Boolean {
            return resOre >= curBlueprint.geodeOre && resObsidian >= curBlueprint.geodeObsidian
        }

        fun getStateNothing(): State {
            var state = this
            while (state.time < maxMin) {
                state = state.mineAndNotBuy()
            }
            return state
        }

        fun getStateOre(): State? {
            if (robOre >= maxOreNeeded) return null
            if ((maxMin - this.time) * (robOre - maxOreNeeded) >= -resOre) return null

            var state = this
            while (state.time < maxMin && !state.canBuyOre()) {
                state = state.mineAndNotBuy()
            }
            if (state.time >= maxMin) return null
            return state.mineAndBuyOre()
        }

        fun getStateClay(): State? {
            if (robClay >= maxClayNeeded) return null
            if ((maxMin - this.time) * (robClay - maxClayNeeded) >= -resClay) return null

            var state = this
            while (state.time < maxMin && !state.canBuyClay()) {
                state = state.mineAndNotBuy()
            }
            if (state.time >= maxMin) return null
            return state.mineAndBuyClay()
        }

        fun getStateObsidian(): State? {
            if (robObsidian >= maxObsidianNeeded) return null
            if ((maxMin - this.time) * (robObsidian - maxObsidianNeeded) >= -resObsidian) return null

            var state = this
            while (state.time < maxMin && !state.canBuyObsidian()) {
                state = state.mineAndNotBuy()
            }
            if (state.time >= maxMin) return null
            return state.mineAndBuyObsidian()
        }

        fun getStateGeode(): State? {
            var state = this
            while (state.time < maxMin && !state.canBuyGeode()) {
                state = state.mineAndNotBuy()
            }
            if (state.time >= maxMin) return null
            return state.mineAndBuyGeode()
        }

        fun getAdjStates(): List<State> {
            if (time >= maxMin)
                return emptyList()

            val leftT = maxMin - time
            val s = leftT * (leftT-1) / 2
            if (s+resGeode <= maxGeodes)
                return emptyList()

            val adj: MutableList<State> = mutableListOf()

            val stateGeode = getStateGeode()
            if (stateGeode != null) adj.add(stateGeode!!)

            val stateObsidian = getStateObsidian()
            if (stateObsidian != null) adj.add(stateObsidian!!)

            val stateClay = getStateClay()
            if (stateClay != null) adj.add(stateClay!!)

            val stateOre = getStateOre()
            if (stateOre != null) adj.add(stateOre!!)

            if (adj.isEmpty())
                adj.add(getStateNothing())

            return adj
        }

        override fun toString(): String {
            return "State: ${time} minutes,\n" +
                    "    resources: ${resOre} ore, ${resClay} clay, ${resObsidian} obsidian, " +
                    "${resGeode} geodes\n" +
                    "    robots: ${robOre} ore, ${robClay} clay, ${robObsidian} obsidian"
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other == null || other!!.javaClass != this.javaClass) return false
            other as State
            return this.time == other.time && this.resOre == other.resOre &&
                    this.resClay == other.resClay && this.resObsidian == other.resObsidian &&
                    this.resGeode == other.resGeode && this.robOre == other.robOre &&
                    this.robClay == other.robClay && this.robObsidian == other.robObsidian
        }
    }

    fun initialState(): State {
        return State(
            0,
            0, 0, 0, 0,
            1, 0, 0
        )
    }

    fun part1(blueprints: List<Blueprint>): Int {
        val debugStates: List<State> = listOf(
            State(21, 15, 25, 4, 9, 3, 6, 4)
        )

        maxMin = part1_minutes
        var ans = 0

        for (blueprint in blueprints) {
            curBlueprint = blueprint

            println("----------------\ncur blueprint: ${curBlueprint.id}")

            maxGeodes = 0
            maxOreNeeded = maxOf(
                maxOf(blueprint.oreOre, blueprint.clayOre),
                maxOf(blueprint.obsidianOre, blueprint.geodeOre)
            )
            maxClayNeeded = blueprint.obsidianClay
            maxObsidianNeeded = blueprint.geodeObsidian

            var q: MutableList<State> = mutableListOf(initialState())
//            var it = 0
            while (q.isNotEmpty()) {
                val state = q.removeLast()
//                it++
//                if (it % 100000 == 0) {
//                    println("it ${it}, state: $state")
//                }
               // println("  cur state: $state")
//                if (state.resGeode > maxGeodes)
//                    println(" got more: $state")

                maxGeodes = maxOf(maxGeodes, state.resGeode)
                for (adj in state.getAdjStates()) {
                    q.add(adj)

                    if (adj in debugStates) {
                        println("Got to state $adj \n   from state $state")
                    }
                }
            }

            ans += maxGeodes * blueprint.id
        }
        return ans
    }

    fun part2(blueprints: List<Blueprint>): Int {
        val debugStates: List<State> = listOf(
            State(21, 15, 25, 4, 9, 3, 6, 4)
        )

        maxMin = 32
        var ans = 1

        for (blueprint in blueprints) {
            curBlueprint = blueprint
            if (curBlueprint.id > 3) break

            println("----------------\ncur blueprint: ${curBlueprint.id}")

            maxGeodes = 0
            maxOreNeeded = maxOf(
                maxOf(blueprint.oreOre, blueprint.clayOre),
                maxOf(blueprint.obsidianOre, blueprint.geodeOre)
            )
            maxClayNeeded = blueprint.obsidianClay
            maxObsidianNeeded = blueprint.geodeObsidian

            var q: MutableList<State> = mutableListOf(initialState())
//            var it = 0
            while (q.isNotEmpty()) {
                val state = q.removeLast()
//                it++
//                if (it % 100000 == 0) {
//                    println("it ${it}, state: $state")
//                }
                // println("  cur state: $state")
//                if (state.resGeode > maxGeodes)
//                    println(" got more: $state")

                maxGeodes = maxOf(maxGeodes, state.resGeode)
                for (adj in state.getAdjStates()) {
                    q.add(adj)

                    if (adj in debugStates) {
                        println("Got to state $adj \n   from state $state")
                    }
                }
            }

            ans *= maxGeodes
        }
        return ans
    }

    fun parseInput(input: List<String>): List<Blueprint> {
        fun parseBlueprint(line: String): Blueprint {
            val parts = line.split(
                "Blueprint ", ": Each ore robot costs ", " ore. Each clay robot costs ",
                " ore. Each obsidian robot costs ", " ore and ",
                " clay. Each geode robot costs ", " ore and ", " obsidian."
            )
            val conv = parts.subList(1, parts.size-1).map { x -> x.toInt() }

           return Blueprint(
               conv[0], conv[1], conv[2],
               conv[3], conv[4],
               conv[5], conv[6]
           )
        }

        val list: MutableList<Blueprint> = mutableListOf()

        for (line in input) {
            list.add(parseBlueprint(line))
        }

        return list
    }

    val filename =
//        "inputs/day19_sample"
        "inputs/day19"

    val input = readInput(filename)
    val blueprints1 = parseInput(input)
    val blueprints2 = parseInput(input)

    println("Part 1: ${part1(blueprints1)}")
    println("Part 2: ${part2(blueprints2)}")
}