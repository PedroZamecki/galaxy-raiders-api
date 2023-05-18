package galaxyraiders.core.physics
import kotlin.math.sqrt

data class Point2D(val x: Double, val y: Double) {
  operator fun plus(p: Point2D): Point2D {
    return Point2D(x + p.x, y + p.y)
  }

  operator fun plus(v: Vector2D): Point2D {
    return Point2D(x + v.dx, y + v.dy)
  }

  override fun toString(): String {
    return "Point2D(x=$x, y=$y)"
  }

  fun toVector(): Vector2D {
    return Vector2D(x, y)
  }

  fun impactVector(p: Point2D): Vector2D {
    return Vector2D(p.x - x, p.y - y)
  }

  fun impactDirection(p: Point2D): Vector2D {
    return this.impactVector(p).unit
  }

  fun contactVector(p: Point2D): Vector2D {
    return Vector2D(p.y - y, x - p.x).unit
  }

  fun contactDirection(p: Point2D): Vector2D {
    return this.contactVector(p)
  }

  fun distance(p: Point2D): Double {
    return sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y))
  }
}