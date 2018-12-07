import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import kotlin.math.abs

//[1518-11-05 00:03] Guard #99 begins shift
//[1518-11-05 00:45] falls asleep
//[1518-11-05 00:55] wakes up
val regex = """\[(.*)] (.*)""".toRegex()
val regexGuard = """Guard #(\d+) begins shift""".toRegex()
val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")

inline class Minute(val minute: Int)
inline class Guard(val guard: Int)

fun main(args: Array<String>) {
    val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    val map = mutableMapOf<Guard, MutableMap<Minute, Int>>()
    var lastMatch: MatchResult? = null

    val sortedBy = allLines
        .map {
            val vals = regex.find(it)!!.groupValues
            dateFormat.parse(vals[1]) to vals[2]
        }
        .sortedBy { it.first }

    println(sortedBy.map { it.first to it.second + '\n' })

    val iterator = sortedBy
        .iterator()


    var guard = Guard(-1)
    while (iterator.hasNext()) {
        val it = iterator.next()

        val guardMatch = regexGuard.find(it.second)
        if (guardMatch != null) {
            guard = Guard(guardMatch.groupValues[1].toInt())
            continue
        }

        val start = Minute(it.first.minutes)
        val end = Minute(iterator.next().first.minutes)

        val guardMins = map.getOrPut(guard) { mutableMapOf() }

        for (min in start.minute until end.minute) {
            guardMins[Minute(min)] = guardMins.getOrDefault(Minute(min), 0) + 1
        }
    }

    val (mostGuard, minutes) = map.entries.maxBy { it.value.map { it.value }.sum() }!!

    val (minute, _) = minutes.maxBy { it.value }!!

    map.entries.forEach {
        println(it)
        println(it.value.map { it.value }.sum())
    }

    println("fin")
    println("${minute.minute}")
    println("${mostGuard.guard}")
    println("${minute.minute * mostGuard.guard}")
}