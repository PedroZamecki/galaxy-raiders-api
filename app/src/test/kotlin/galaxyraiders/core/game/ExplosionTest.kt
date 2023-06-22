package galaxyraiders.core.game

import galaxyraiders.core.physics.Point2D
import galaxyraiders.core.physics.Vector2D
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Given an explosion")
class ExplosionTest {
  private val explosion = Explosion(
    initialPosition = Point2D(1.0, 1.0)
  )

  @Test
  fun `it has a type Explosion `() {
    assertEquals("Explosion", explosion.type)
  }

  @Test
  fun `it has a symbol at `() {
    assertEquals('*', explosion.symbol)
  }

  @Test
  fun `It shows the type Explosion when converted to String `() {
    assertTrue(explosion.toString().contains("Explosion"))
  }

  @Test
  fun `Can collide with another object `() {
    val otherObject = SpaceShip(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )

    assertTrue(explosion.impacts(otherObject))
  }

  @Test
  fun `It doesn't move `() {
    val initialPosition = explosion.center

    explosion.move()

    assertEquals(initialPosition, explosion.center)  
  }
}
