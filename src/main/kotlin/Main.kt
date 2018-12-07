import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val chars = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).first().toMutableList()

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

        if (!reacted) break
    }

    val ret = chars.fold("") { acc, c -> acc + c }

    println("fin")
    println(ret.length)
}

private fun Char.reactsWith(last: Char) = if (isUpperCase()) {
    last.isLowerCase() && equals(last.toUpperCase())
} else {
    last.isUpperCase() && equals(last.toLowerCase())
}
