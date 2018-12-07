import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val chars = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).first()

    val ret = ('a'..'z').map {
        println(it)
        compute(chars.replace(it.toString(), "", true).toMutableList()).size
    }
        .minBy { it }

    println("fin")
    println(ret)
}

private fun compute(chars: MutableList<Char>): MutableList<Char> {
    while (true) {
        var last = '0'
        var reacted = false
        for (i in 0 until chars.size) {
            if (chars[i].reactsWith(last)) {
                chars.removeAt(i)
                chars.removeAt(i - 1)
                reacted = true
                break
            }
            last = chars[i]
        }

        if (!reacted) return chars
    }
}

private fun Char.reactsWith(last: Char) = if (isUpperCase()) {
    last.isLowerCase() && equals(last.toUpperCase())
} else {
    last.isUpperCase() && equals(last.toLowerCase())
}
