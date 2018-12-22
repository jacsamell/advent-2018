val regex = """[<>^v]""".toRegex()

private val target = "165061"

private val digits = target.length

fun main(args: Array<String>) {

    val list = mutableListOf(3, 7)

    var elf1 = 0
    var elf2 = 1

    while (true) {
        val sum = list[elf1] + list[elf2]
        if (sum > 9) {
            list.add(1)
            list.add(sum - 10)
        } else {
            list.add(sum)
        }

        elf1 += list[elf1] + 1
        elf2 += list[elf2] + 1

        while (elf1 >= list.size) elf1 -= list.size
        while (elf2 >= list.size) elf2 -= list.size

        val size = list.size
        if (size > digits) {
            for (s in size - 1..size) {
                var last = ""
                for (i in s - digits until s) {
                    last += list[i]
                }

                if (last == target) {
                    println(s - digits)
                    return
                }
            }
        }
    }
}
