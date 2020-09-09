import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Firework(var pos: Vector2, var vel: Vector2, var fuse: Double, val color: ColorHSVa, val explode: Boolean = false)

fun main() = application {
    configure {
        width = 900
        height = 600
    }

    program {
        val fireworks = mutableListOf<Firework>()
        val fragCount = 50
        val theta = 2.0 * PI / fragCount
        var delay = 1.0

        extend {
            if (seconds > delay) {
                delay += .2

                fireworks += Firework(
                        Vector2(width / 2.0, height.toDouble()),
                        Vector2(Random.nextDouble(-10.0, 10.0), -Random.nextDouble(10.0, 13.0)),
                        Random.nextDouble(.6, .8),
                        ColorHSVa(Random.nextDouble(360.1), .5, 1.0),
                        true
                )
            }

            (0 until fireworks.size).forEach {
                val f = fireworks[it]
                drawer.stroke = f.color.toRGBa()
                drawer.circle(f.pos, 2.0)

                f.pos += f.vel
                f.fuse -= deltaTime

                if (f.fuse < .0 && f.explode) {
                    (0..fragCount).forEach { frag ->
                        fireworks += Firework(
                                f.pos,
                                Vector2(cos(theta * frag), sin(theta * frag)) * 2.0,
                                Random.nextDouble(.4, .6),
                                f.color.shiftHue(Random.nextDouble(-25.0, 25.0))
                        )
                    }
                }
            }

            fireworks.removeIf { it.fuse <= .0 }
        }
    }
}