import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Firework(var pos: Vector2, var vel: Vector2, var fuse: Double, var color: ColorHSVa, val explode: Boolean = false) {
    fun update(drawer: Drawer, time: Double) {
        if (fuse > .0) {
            drawer.stroke = this.color.toRGBa()

            drawer.circle(this.pos, 2.0)

            pos += this.vel
            fuse -= time
        }
    }
}

fun main() = application {
    configure {
        width = 900
        height = 600
    }

    program {
        val fireworks = mutableListOf<Firework>()
        val delay = 1.0
        var time = .0

        extend {
            time += seconds

            if (time > delay) {
                time -= delay

                fireworks.add(
                        Firework(
                                Vector2(width / 2.0, height.toDouble()),
                                Vector2(Random.nextDouble(-10.0, 10.0), -Random.nextDouble(10.0, 15.0)),
                                Random.nextDouble(.6, .8),
                                ColorHSVa(Random.nextDouble(.0, 360.1), .5, 1.0),
                                true
                        )
                )

                (0 until fireworks.size).forEach {
                    val f = fireworks[it]
                    f.update(drawer, deltaTime)

                    if (f.fuse < .0 && f.explode) {
                        val theta = 2.0 * PI / 50

                        (0..50).forEach { frag ->
                            fireworks.add(
                                    Firework(
                                            f.pos,
                                            Vector2(cos(theta * frag), sin(theta * frag)) * 2.0,
                                            Random.nextDouble(.4, .6),
                                            f.color
                                    )
                            )
                        }
                    }
                }
            }

            fireworks.removeIf { it.fuse <= .0 }
        }
    }
}