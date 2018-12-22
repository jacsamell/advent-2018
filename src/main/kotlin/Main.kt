val regex = """[<>^v]""".toRegex()

private val count = 165061

fun main(args: Array<String>) {

    val list = mutableListOf(3, 7)

    var elf1 = 0
    var elf2 = 1

    while (list.size < count + 10) {
        val sum = list[elf1] + list[elf2]
        if (sum > 9) {
            val digit = sum / 10
            list.add(digit)
            list.add(sum - digit * 10)
        } else {
            list.add(sum)
        }

        elf1 += list[elf1]+1
        elf2 += list[elf2]+1

        while (elf1 >= list.size) elf1 -= list.size
        while (elf2 >= list.size) elf2 -= list.size

        //println(list)
    }

    for (i in count..count + 9)
        print(list[i])
}
