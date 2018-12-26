import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

/*
Before: [2, 3, 2, 2]
15 3 2 2
After:  [2, 3, 4, 2]
 */

val regexBefore = """Before: \[(\d), (\d), (\d), (\d)]""".toRegex()
val regexOp = """(\d) (\d) (\d) (\d)""".toRegex()
val regexAfter = """After:  \[(\d), (\d), (\d), (\d)]""".toRegex()

lateinit var tests: List<Test>

data class Test(val before: Map<Int, Register>, val op: Operation, val after: Map<Int, Register>)

data class Operation(val op: Int, val a: Int, val b: Int, val reg: Int)

fun main(args: Array<String>) {
    tests = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))
        .filter { it.isNotBlank() }
        .chunked(3)
        .map {
            val before = regexBefore.find(it[0])!!.groupValues.drop(1).map { it.toInt() }
                .mapIndexed { index, it -> index to Register(it) }.toMap()
            val op = regexOp.find(it[1])!!.groupValues.drop(1).map { it.toInt() }
            val after = regexAfter.find(it[2])!!.groupValues.drop(1).map { it.toInt() }
                .mapIndexed { index, it -> index to Register(it) }.toMap()

            Test(before, Operation(op[0], op[1], op[2], op[3]), after)
        }

    var over3 = 0

    tests.forEachIndexed { index, test ->
        var count = 0

        allOps.forEach { op ->
            registers = test.before.mapValues { it.value.copy() }.toMutableMap()
            operate(op, test.op.a, test.op.b, register(test.op.reg))
            if (registers == test.after) {
                //println(op)
                count++
            }
        }

        println("$index:$count")

        if (count >= 3) over3++
    }

    println(over3)
}

fun register(code: Int): Register {
    return registers[code]!!
}

fun reg(code: Int): Int {
    return registers[code]!!.vaule
}

var registers: MutableMap<Int, Register> = mutableMapOf()

fun operate(op: String, valueA: Int, valueB: Int, reg: Register) {
    reg.vaule = when (op) {
        "addr" -> reg(valueA) + reg(valueB)
        "addi" -> reg(valueA) + valueB
        "mulr" -> reg(valueA) * reg(valueB)
        "muli" -> reg(valueA) * valueB
        "banr" -> reg(valueA) and reg(valueB)
        "bani" -> reg(valueA) and valueB
        "borr" -> reg(valueA) or reg(valueB)
        "bori" -> reg(valueA) or valueB
        "setr" -> reg(valueA)
        "seti" -> valueA
        "gtir" -> if (valueA > reg(valueB)) 1 else 0
        "gtri" -> if (reg(valueA) > valueB) 1 else 0
        "gtrr" -> if (reg(valueA) > reg(valueB)) 1 else 0
        "eqir" -> if (valueA == reg(valueB)) 1 else 0
        "eqri" -> if (reg(valueA) == valueB) 1 else 0
        "eqrr" -> if (reg(valueA) == reg(valueB)) 1 else 0
        else -> throw RuntimeException()
    }
}

val allOps = arrayOf(
    "addr",
    "addi",
    "mulr",
    "muli",
    "banr",
    "bani",
    "borr",
    "bori",
    "setr",
    "seti",
    "gtir",
    "gtri",
    "gtrr",
    "eqir",
    "eqri",
    "eqrr"
)

data class Register(var vaule: Int)