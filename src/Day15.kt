import kotlin.math.abs

fun main() {

    open class Point(val x: Int, val y: Int)
    class Sensor(x: Int, y: Int, val beacon: Point) : Point(x, y)

    fun dist(a: Point, b: Point): Int {
        return abs(a.x - b.x) + abs(a.y - b.y)
    }

    fun canBeBeacon(pos: Point, sensors: List<Sensor>): Boolean {
        for (sensor in sensors) {
            if (sensor.beacon.x == pos.x && sensor.beacon.y == pos.y)
                return true

            if (dist(sensor, pos) <= dist(sensor, sensor.beacon))
                return false
        }

        return true
    }

    fun part1(sensors: List<Sensor>, y: Int): Int {
        var ans = 0

        for (x in -10000000..10000000) {
            if (!canBeBeacon(Point(x, y), sensors))
                ans++
        }

        return ans
    }

    fun part2(sensors: List<Sensor>, maxCoord: Int): Long {
        fun tuningFrequency(x: Int, y: Int): Long {
            val xLong = x.toLong()
            val yLong = y.toLong()
            return xLong * 4000000L + yLong
        }

        fun updateY(x: Int, origY: Int): Int {
            var y = origY
            for (sensor in sensors) {
                if (dist(sensor, Point(x, y)) <= dist(sensor, sensor.beacon)) {
                    // inside sensor's range
                    y = sensor.y + dist(sensor, sensor.beacon) + 1 - abs(sensor.x - x)
                }
            }
            return y
        }

        var last: Point

        for (x in 0..maxCoord) {
            var y = 0
            last = Point(x, y)
            while (y <= maxCoord) {
                y = updateY(x, y)
                if (x == last.x && y == last.y)
                    return tuningFrequency(x, y)
                last = Point(x, y)
            }
        }

        return -1
    }

    fun parseSensor(line: String): Sensor {
        val parts = line.split(
            "Sensor at x=", ", y=", ": closest beacon is at x="
        )
        val sensorX = parts[1].toInt()
        val sensorY = parts[2].toInt()
        val beaconX = parts[3].toInt()
        val beaconY = parts[4].toInt()
        return Sensor(sensorX, sensorY, Point(beaconX, beaconY))
    }

    fun parseSensors(input: List<String>): List<Sensor> {
        val sensors: MutableList<Sensor> = mutableListOf()
        for (line in input)
            sensors.add(parseSensor(line))
        return sensors
    }

    val filename =
//        "inputs/day15_sample"
        "inputs/day15"
    val lineY = // part1
//        10
        2000000
    val searchSpace = // part2
//        20
        4000000

    val input = readInput(filename)
    val sensors1 = parseSensors(input)
    val sensors2 = parseSensors(input)

    println("Part 1: ${part1(sensors1, lineY)}")
    println("Part 2: ${part2(sensors2, searchSpace)}")
}