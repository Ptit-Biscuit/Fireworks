import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.*
import kotlin.random.Random

class Firework(width: Int, height: Int, seconds: Double) {
    var pos = Vector2(width / 2.0 + Random.nextDouble(-width / 5.0, width / 5.0), height.toDouble())
    var vel = Vector2(Random.nextDouble(-.5, .5), -Random.nextDouble(8.0, 10.0))
    var fuse = seconds + Random.nextDouble(30.0, 40.0)
    var color = ColorHSVa(Random.nextDouble(.0, 360.1), .5, 1.0)
    var explode = true

    constructor(
        width: Int,
        height: Int,
        seconds: Double,
        pos: Vector2,
        vel: Vector2,
        fuse: Double,
        color: ColorHSVa
    ) : this(width, height, seconds) {
        this.pos = pos
        this.vel = vel
        this.fuse = fuse
        this.color = color
        this.explode = false
    }

    fun update(drawer: Drawer, seconds: Double) {
        if (this.fuse > .0) {
            drawer.stroke = this.color.toRGBa()

            drawer.circle(this.pos, 2.0)

            this.pos += this.vel
            this.fuse -= seconds
        }
    }
}

fun main() = application {
    configure {
        width = 900
        height = 600
    }

    program {
        val fireworks = MutableList(1) { Firework(width, height, seconds) }

        mouse.buttonDown.listen { fireworks.add(Firework(width, height, seconds)) }

        extend {
            (0 until fireworks.size).forEach {
                val f = fireworks[it]
                f.update(drawer, seconds)

                if (f.fuse < .0 && f.explode) {
                    (0..20).forEach { frag ->
                        fireworks.add(
                            Firework(
                                width,
                                height,
                                seconds,
                                f.pos,
                                Vector2(asin(Random.nextDouble()), acos(Random.nextDouble())),
                                seconds + 100.0,
                                f.color
                            )
                        )
                    }
                }
            }
            fireworks.removeIf { it.fuse < .0 }
        }
    }
}