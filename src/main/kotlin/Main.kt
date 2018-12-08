import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val input = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).single()

    val nums = input.split(' ')
        .map { it.toInt() }
        .iterator()

    val root = readNode(nums)

    if (nums.hasNext()) throw RuntimeException("Not read all data")

    val ret = calculate(root)

    println("fin")
    println(ret)
}

private fun calculate(node: Node): Int {
    return node.metas.sum() + node.children.map { calculate(it) }.sum()
}

private fun readNode(nums: Iterator<Int>): Node {
    val childCount = nums.next()
    val metaCount = nums.next()

    val children = mutableListOf<Node>()
    for (i in 0 until childCount) {
        children.add(readNode(nums))
    }

    val meta = mutableListOf<Int>()
    for (i in 0 until metaCount) {
        meta.add(nums.next())
    }

    return Node(meta, children)
}

private data class Node(val metas: List<Int>, val children: List<Node>)