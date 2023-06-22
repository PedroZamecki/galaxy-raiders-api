package galaxyraiders.core.game

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.*
import java.lang.Exception
import java.io.File

public val scoreboardPath = "core/score/Scoreboard.json"
public val leaderboardPath = "core/score/Leaderboard.json"

class Score(
    date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
    score: Double = 0.0, 
    asteroidsDestroyed: Int = 0) {
  private val date = date
  private val time = time
  private var score = score
  private var asteroidsDestroyed = asteroidsDestroyed

  private fun tryToCreateScoreboardFile(): Boolean {
    val scoreFile = File(scoreboardPath)
    if (!scoreFile.exists()) {
      try {
        scoreFile.createNewFile()
      } catch (e: Exception) {
        println("Error creating the Scoreboard file")
        return true
      }
    }
    return false
  }

  private fun tryToCreateLeaderboardFile(): Boolean {
    val leaderboardFile = File(leaderboardPath)
    if (!leaderboardFile.exists()) {
      try {
        leaderboardFile.createNewFile()
      } catch (e: Exception) {
        println("Error creating the Leaderboard file")
        return true
      }
    }
    return false
  }  

  fun getDate(): String {
    return this.date
  }

  fun getTime(): String {
    return this.time
  }

  fun getScore(): Double {
    return this.score
  }

  fun getAsteroidsDestroyed(): Int {
    return this.asteroidsDestroyed
  }

  private fun scoreboardJSON(): String {
    return """
      {
              "Date": "$date",
              "Time": "$time",
              "Score": $score,
              "Asteroids Destroyed": $asteroidsDestroyed""".trimIndent()
  }

  private fun readJSON(JSONFile: File): List<Score> {
    val scoreFileReader = JSONFile.reader()
    val scoreboard = scoreFileReader.readText()

    var scores = listOf<Score>()

    // If the file is empty, return an empty list
    if (scoreboard == "") {
      return scores
    }

    // Make a PARSER to read the JSON
    // Knowing that the JSON is always the same
    // and the order of the fields is always the same
    // we can use a simple split to get the values
    // Example of JSON:
    // [
    //   {
    //     "Date": "01/01/2021",
    //     "Time": "00:00:00",
    //     "Score": 0.0,
    //     "Asteroids Destroyed": 0
    //   }
    // ]
    val scoreboardArray = scoreboard.split("},\n")
    for (score in scoreboardArray) {
      val scoreArray = score.split(",\n")

      val dateValue = scoreArray[0].split(": ")[1].replace("\"", "")
      val timeValue = scoreArray[1].split(": ")[1].replace("\"", "")
      val scoreValue = scoreArray[2].split(": ")[1].toDouble()
      val asteroidsValue = scoreArray[3].split(": ")[1].filter(Char::isDigit).toInt()
      scores += Score(
        dateValue, 
        timeValue, 
        scoreValue, 
        asteroidsValue)
    }

    scoreFileReader.close()
    return scores
  }

  private fun writeJSON(JSONFile: File, scores: List<Score>) {
    val scoreFileWriter = JSONFile.writer()
    scoreFileWriter.write("[\n")
    for (score in scores) {
      scoreFileWriter.write("\t" + score.scoreboardJSON())
      if (score != scores.last())
        scoreFileWriter.write("\n\t},\n")
      else
        scoreFileWriter.write("\n\t}\n")
    }

    scoreFileWriter.write("]")
    scoreFileWriter.close()
  }

  fun tryToReadScoreboard(): List<Score> {
    if (tryToCreateScoreboardFile()) {
      return listOf<Score>()
    }
    val scoreFile = File(scoreboardPath)
    return readJSON(scoreFile)
  }

  fun tryToReadLeaderboard(): List<Score> {
    if (tryToCreateLeaderboardFile()) {
      return listOf<Score>()
    }
    val leaderboardFile = File(leaderboardPath)
    return readJSON(leaderboardFile)
  }

  fun addScore(asteroidDestroyed: Asteroid) {
    this.score += asteroidDestroyed.mass * asteroidDestroyed.mass / asteroidDestroyed.radius
    this.asteroidsDestroyed += 1
  }

  fun writeScore(): String {
    var scores = tryToReadScoreboard() + this
    val scoreFile = File(scoreboardPath)
    writeJSON(scoreFile, scores)
    checkForLeaderboard()
    return scoreFile.absolutePath
  }

  private fun checkForLeaderboard() {
    // Check all the scoreboard entries and put the best 3 of them in the leaderboard
    // If there are less than 3 entries, put all of them in the leaderboard
    // If there are no entries, do nothing

    tryToCreateLeaderboardFile()
    val scores = tryToReadScoreboard()
    
    // filter the best 3 scores
    val bestScores = scores.sortedByDescending { it.getScore() }.take(3)
    val leaderboardFile = File(leaderboardPath)
    writeJSON(leaderboardFile, bestScores)
  }

  fun cleanScoreboard(): Boolean {
    if (tryToCreateScoreboardFile())
      return true
    val scoreFile = File(scoreboardPath)
    scoreFile.writeText("")
    return true
  }

  fun cleanLeaderboard(): Boolean {
    if (tryToCreateLeaderboardFile())
      return true
    val leaderboardFile = File(leaderboardPath)
    leaderboardFile.writeText("")
    return true
  }
}
