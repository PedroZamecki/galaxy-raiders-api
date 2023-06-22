package galaxyraiders.core.game

import galaxyraiders.core.physics.Point2D
import galaxyraiders.core.physics.Vector2D
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.io.File

@DisplayName("Given a score object")
class ScoreTest {
  private val score = Score()

  @Test
  fun `it has a score of 0 `() {
    assertEquals(0.0, score.getScore())
  }

  @Test
  fun `it adds score correctly `() {
    val asteroid = Asteroid(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )

    score.addScore(asteroid)

    assertEquals(1.0, score.getScore())
  }

  @Test
  fun `it adds score correctly when there are more than one asteroid `() {
    val asteroid = Asteroid(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )

    score.addScore(asteroid)
    score.addScore(asteroid)

    assertEquals(2.0, score.getScore())
  }

  @Test
  fun `it writes the score in the Scoreboard file `() {
    val asteroid = Asteroid(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )

    score.addScore(asteroid)
    val scoreboardPath = score.writeScore()

    // Lê o arquivo Scoreboard.json e verifica seus dados
    assertTrue(File(scoreboardPath).exists())
  }

  @Test
  fun `it returns the scoreboards as a list`() {
    val asteroid = Asteroid(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )
    score.addScore(asteroid)
    score.writeScore()

    val newTestScore = Score(
      date = "01/01/2021",
      time = "00:00:00",
      score = 1.0,
      asteroidsDestroyed = 1
    )
    newTestScore.writeScore()

    val scoreboards = score.tryToReadScoreboard()
    assertAll(
      "it should append the new score to the list",
      { assertEquals(1.0, scoreboards.last().getScore()) }
    )
  }

  @Test
  fun `it writes the score in the Leaderboard file `() {
    val asteroid = Asteroid(
      initialPosition = Point2D(1.0, 1.0),
      initialVelocity = Vector2D(0.0, 0.0),
      radius = 1.0,
      mass = 1.0
    )

    score.addScore(asteroid)
    score.writeScore()

    assertTrue(File(leaderboardPath).exists())
  }

  @Test
  fun `it cleans the scoreboard`() {
    score.cleanScoreboard()
    val scoreboards = score.tryToReadScoreboard()
    assertAll(
      "it should clean the scoreboard",
      { assertEquals(0, scoreboards.size) },
      { assertTrue(score.cleanScoreboard())}
    )
  }

  @Test
  fun `it cleans the leaderboard`(){
    score.cleanLeaderboard()
    val leaderboards = score.tryToReadLeaderboard()
    assertAll(
      "it should clean the leaderboard",
      { assertEquals(0, leaderboards.size) },
      { assertTrue(score.cleanLeaderboard())}
    )
  }
}
