/**
 *  Dice Royale - Kotlin Console Game
 *  Description:
 *  A dice betting game where a player competes against
 *  the computer across a chosen number of rounds.
 *
 *  Features:
 *  - Input validation for name, rounds, and bets
 *  - Special dice outcomes (Snake Eyes & Double Sixes)
 *  - Bankruptcy detection
 *  - Early quit option
 *  - Persistent top-3 leaderboard per round count
 *
 *  Author: Molly O'Connor
 *  Date: 2/19/2026
 *
 *  References:
 *  - All initial print statements were written by author
 *  - ChatGPT was used for assistance with adding emojis, and ANSI color formatting
 *  - Kotlin Official Documentation: https://kotlinlang.org/docs
 *    (used to understand ranges, loops, collections, and file I/O)
 */

import java.io.File
import kotlin.random.Random

//  GLOBAL CONSTANTS & DATA STRUCTURES

// File path where leaderboard data is stored
val SCOREBOARD_FILE = "src/scoreboard.txt"

// Map Structure:
// Key -> Number of rounds (Int, 1-10)
// Value -> List of top 3 scores for that round count (Name:score)
val scoreboard: MutableMap<Int, MutableList<Pair<String, Int>>> = mutableMapOf()


//  SCOREBOARD FUNCTIONS

/**
 * Loads scoreboard data from the text file into memory.
 * File format example:
 *
 * rounds:3
 * John:250
 * Alice:180
 * Molly:130
 *
 * Each "rounds:X" section represents a different leaderboard.
 */
fun loadScoreboard() {
    val file = File(SCOREBOARD_FILE)
    if (!file.exists()) return  // If file doesn't exist, nothing to load

    var currentRounds = 0

    file.forEachLine { line ->
        val trimmed = line.trim()

        // Detect new rounds section
        if (trimmed.startsWith("rounds:")) {
            currentRounds = trimmed.removePrefix("rounds:").toInt()
            scoreboard[currentRounds] = mutableListOf()
        }

        // Parse player scores
        else if (trimmed.contains(":") && currentRounds > 0) {
            val parts = trimmed.split(":")
            val name = parts[0]
            val score = parts[1].toIntOrNull() ?: 0
            scoreboard[currentRounds]?.add(name to score)
        }
    }
}


/**
 * Saves the current in-memory leaderboard to the text file.
 * Overwrites the file each time it is called.
 */
fun saveScoreboard() {
    val file = File(SCOREBOARD_FILE)

    file.printWriter().use { out ->
        scoreboard.forEach { (rounds, list) ->
            out.println("rounds:$rounds")

            list.forEach { (name, score) ->
                out.println("$name:$score")
            }

            out.println() // Blank line between round sections
        }
    }
}


/**
 * Updates the leaderboard for a specific number of rounds.
 * Keeps only the top 3 highest scores.
 */
fun updateScoreboard(rounds: Int, name: String, money: Int) {
    val list = scoreboard.getOrPut(rounds) { mutableListOf() }

    list.add(name to money)
    list.sortByDescending { it.second }

    if (list.size > 3) list.removeLast()

    saveScoreboard()
}


/**
 * Displays the top 3 scores for the selected round count.
 */
fun printScoreboard(rounds: Int) {
    val list = scoreboard[rounds]

    println("\n\u001B[1;34m🏆 Top Scores for $rounds Round(s) 🏆\u001B[0m")

    if (list.isNullOrEmpty()) {
        println("No scores yet!")
    } else {
        list.forEachIndexed { index, (name, score) ->
            println("${index + 1}. $name - $$score")
        }
    }
}


//  INPUT VALIDATION FUNCTIONS

/**
 * Prompts user for a valid name.
 * - Letters and spaces only
 * - Maximum 25 characters
 */
fun getValidName(): String {
    while (true) {
        print("Enter your name (letters and spaces only, max 25 characters): ")
        val input = readlnOrNull()

        if (input.isNullOrBlank())
            println("\u001B[31mInvalid name. Please enter text before starting.\u001B[0m")
        else if (input.length > 25)
            println("\u001B[31mInvalid name (max length of 25 characters).\u001B[0m")
        else if (!input.all { it.isLetter() || it.isWhitespace() })
            println("\u001B[31mInvalid name (must contain letters and spaces only).\u001B[0m")
        else return input.trim()
    }
}


/**
 * Prompts user for number of rounds.
 * - Has to be a number (1-10)
 */
fun getValidRounds(): Int {
    while (true) {
        print("How many rounds would you like to play? (1-10) ")
        val input = readlnOrNull()
        val number = input?.toIntOrNull()

        if (number == null)
            println("\u001B[31mInvalid rounds. Please enter a number.\u001B[0m")
        else if (number !in 1..10)
            println("\u001B[31mInvalid rounds. Please enter a number between 1 and 20.\u001B[0m")
        else return number
    }
}


/**
 * Prompts user for a valid betting amount.
 * - Must be numeric
 * - Must be >= 1
 * - Cannot exceed available money
 */
fun getValidBet(money: Int): Int {
    while (true) {
        print("Enter your bet (Available: $$money): ")
        val input = readlnOrNull()
        val bet = input?.toIntOrNull()

        if (bet == null)
            println("\u001B[31mInvalid bet. Please enter a number.\u001B[0m")
        else if (bet < 1)
            println("\u001B[31mBet must be greater than 0.\u001B[0m")
        else if (bet > money)
            println("\u001B[31mYou cannot bet more money than you currently have ($$money).\u001B[0m")
        else return bet
    }
}


//  CORE GAME LOGIC

/**
 * Executes one full round of the game.
 * Returns the updated amount of money after the round.
 */
fun playRound(money: Int): Int {

    println("\n\u001B[1;34m==============================\u001B[0m")
    println("\u001B[1;34m💰 Current money: $$money\u001B[0m")
    println("\u001B[1;34m==============================\u001B[0m\n")

    val bet = getValidBet(money)
    println("\nYou bet: \u001B[1;36m$$bet\u001B[0m\n")

    // Player First Roll
    println("\u001B[36m🎲 Press Enter to roll your FIRST die...\u001B[0m")
    readlnOrNull()
    val playerDie1 = Random.nextInt(1, 7)
    println("\u001B[36mYour first roll: $playerDie1\u001B[0m\n")

    // Computer First Roll
    println("\u001B[33m🤖 Computer rolling first die...\u001B[0m")
    // Add a pause to make it feel more game like
    Thread.sleep(1000)
    val computerDie1 = Random.nextInt(1, 7)
    println("\u001B[33mComputer rolled: $computerDie1\u001B[0m\n")

    // Player Second Roll
    println("\u001B[36m🎲 Press Enter to roll your SECOND die...\u001B[0m")
    readlnOrNull()
    val playerDie2 = Random.nextInt(1, 7)
    println("\u001B[36mYour second roll: $playerDie2\u001B[0m\n")

    // Computer Second Roll
    println("\u001B[33m🤖 Computer rolling second die...\u001B[0m")
    Thread.sleep(1000)
    val computerDie2 = Random.nextInt(1, 7)
    println("\u001B[33mComputer rolled: $computerDie2\u001B[0m\n")

    // Compute totals
    val playerTotal = playerDie1 + playerDie2
    val computerTotal = computerDie1 + computerDie2

    // Add a pause to make it more game like
    println("Calculating results...")
    Thread.sleep(800)

    // Display final sums
    println("\u001B[36m------------------------------\u001B[0m")
    println("\u001B[36m🎲 Your total: $playerTotal\u001B[0m")
    println("\u001B[33m🤖 Computer total: $computerTotal\u001B[0m")
    println("\u001B[36m------------------------------\u001B[0m")

    // Snake eyes
    if (playerDie1 == 1 && playerDie2 == 1) {
        println("\u001B[1;31m💀 DISASTER! Snake Eyes! You lose everything!\u001B[0m")
        return 0
    }

    // 2 sixes bonus
    if (playerDie1 == 6 && playerDie2 == 6) {
        println("\u001B[1;33m🎉 JACKPOT! Double sixes! You win 3x your bet!\u001B[0m")
        return money + (bet * 3)
    }

    // Standard Win/Loss Logic
    return if (playerTotal > computerTotal) {
        println("\u001B[1;36m🏆 You win the round! +$$bet\u001B[0m")
        money + bet
    } else if (playerTotal < computerTotal) {
        println("\u001B[1;35m😢 You lose the round! -$$bet\u001B[0m")
        money - bet
    } else {
        println("\u001B[1;33m🤝 It's a tie! No money lost or gained.\u001B[0m")
        money
    }
}


//  MAIN GAME LOOP
/**
 * Main entry point of the Dice Royale program.
 *
 * Responsibilities:
 * - Loads the saved leaderboard from file.
 * - Displays game instructions.
 * - Handles the full game lifecycle (player setup, round loop, bankroll tracking).
 * - Detects bankruptcy and early quit conditions.
 * - Updates and displays the leaderboard if the player completes all rounds.
 * - Allows the user to restart the game.
 */
fun main() {

    // Load leaderboard from file at program start
    loadScoreboard()

    println("\u001B[1;34m🎲 Welcome to Dice Royale! 🎲\u001B[0m")

    // Game Instructions
    println("\n\u001B[1;33mInstructions:\u001B[0m")
    println("1. You start with $100.")
    println("2. Before each round, place a bet on your roll.")
    println("3. You and the computer each roll two dice.")
    println("4. Highest total wins the round. Tie = no change in money.")
    println("5. Special rolls:")
    println("   - Snake Eyes (1+1) = you lose all your money!")
    println("   - Double Sixes (6+6) = win 3x your bet!")
    println("6. If you go bankrupt, the game ends immediately.")
    println("7. After each round, you can choose to continue or quit.\n")

    var playAgain: Boolean

    // Outer loop allows full game restart
    do {

        // Get username and number of rounds
        val name = getValidName()
        val rounds = getValidRounds()

        // Print scoreboard for chosen rounds
        println("\n\u001B[1;33mCurrent leaderboard for $rounds round(s):\u001B[0m")
        printScoreboard(rounds)

        // start with $100 in bank
        var money = 100
        var quitGame = false

        // Welcome user
        println("\n\u001B[1;33m==============================\u001B[0m")
        println("\u001B[1;33mWelcome $name!\u001B[0m")
        println("\u001B[1;33mYou will play $rounds rounds.\u001B[0m")
        println("\u001B[1;33m==============================\u001B[0m")

        // Round Loop
        // 'rounds' is an int, for loop requires iterable range so we make it '1..rounds'
        for (round in 1..rounds) {

            if (quitGame) break

            println("\n\u001B[1;34m--- ROUND $round ---\u001B[0m")
            money = playRound(money)

            // Bankrupt logic
            if (money <= 0) {
                println("\n\u001B[1;31m💀 You are bankrupt!\u001B[0m")
                quitGame = true
                break
            }

            // Prompt to continue or quit early
            if (round < rounds) {
                while (true) {
                    println("\nPress Enter to continue, or type 'x' to quit:")
                    val input = readlnOrNull()?.trim()

                    if (input.isNullOrEmpty()) break
                    else if (input.lowercase() == "x") {
                        // Don't do any scoreboard logic if they quit early
                        println("\n\u001B[1;33mYou chose to quit the game early. Scores will not be recorded.\u001B[0m")
                        quitGame = true
                        break
                    } else {
                        println("Invalid input. Press Enter to continue, or 'x' to quit.")
                    }
                }
            }
        }

        // Only update leaderboard if player finished normally
        if (!quitGame) {
            updateScoreboard(rounds, name, money)
            printScoreboard(rounds)
        }

        // Game over display
        println("\n\u001B[1;34m==============================\u001B[0m")
        println("\u001B[1;34mGAME OVER \u001B[0m")
        println("\u001B[1;34mFinal money: $$money\u001B[0m")
        println("\u001B[1;34m==============================\u001B[0m")

        // Ask to restart entire game
        while (true) {
            println("\nDo you want to play again? (y/n)")
            val againInput = readlnOrNull()?.trim()?.lowercase()

            if (againInput == "y") {
                playAgain = true
                break
            } else if (againInput == "n") {
                playAgain = false
                println("\nThanks for playing Dice Royale! 🎲")
                break
            } else {
                println("Invalid input. Please type 'y' for yes or 'n' for no.")
            }
        }

    } while (playAgain)
}








