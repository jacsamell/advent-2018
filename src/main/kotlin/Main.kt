import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val input = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).single()

    val nums = input.split(' ').map { it.toInt() }.iterator()

    val root = readNode(nums)

    val result = calculate(root)

    println(result)
}

private fun calculate(node: Node): Int = if (node.children.isEmpty()) {
    node.metas.sum()
} else {
    node.metas.map { i -> node.children.getOrNull(i - 1) }.filterNotNull().sumBy { calculate(it) }
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