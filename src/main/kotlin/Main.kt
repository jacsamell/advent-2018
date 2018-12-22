import java.awt.Component
import java.awt.Graphics
import java.awt.Rectangle
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JFrame

//position=<-21751,  11136> velocity=< 2, -1>
val regex = """position=< ?(-?\d+),  ?(-?\d+)> velocity=< ?(-?\d+),  ?(-?\d+)>""".toRegex()

var scale = 10

var i = 0

fun main(args: Array<String>) {
    val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))


    val points = allLines.map {
        val result = regex.find(it)!!.groupValues
        Point(result[1].toInt(), result[2].toInt(), result[3].toInt(), result[4].toInt())
    }

    val frame = object : JFrame() {
        override fun paint(g: Graphics) {
            g.clearRect(0,0,2000,2000)

            points.forEach { it.paint(g) }
        }
    }

    frame.setSize(2000, 2000)
    frame.isVisible = true

    while (true) {
        println("Hit enter")

        frame.repaint()

        points.forEach {
            it.increment()
        }
        i++
        println(i)

        Thread.sleep(1)
    }
}

data class Point(var posX: Int, var posY: Int, var vX: Int, var vY: Int) {
    fun increment() {
        posX += vX
        posY += vY
    }

    fun paint(g: Graphics) {
        g.fillRect((posX * 5).toInt(), (posY * 5).toInt(), 10, 10)
    }
}