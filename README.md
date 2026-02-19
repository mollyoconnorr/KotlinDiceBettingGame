#  Kotlin Dice Betting Game

## Introduction

The **Kotlin Dice Betting Game** is a console-based betting game written
in Kotlin.\
Players choose how many rounds they want to play and place bets against
the computer.\
Both the player and the computer roll dice each round, and the higher
roll wins the bet.

------------------------------------------------------------------------

##  Features Implemented

-   Input validation
-   Early quit handling
-   Bankruptcy detection
-   Persistent file storage
-   Leaderboards separated by round count
   
------------------------------------------------------------------------

##  How to Play

1.  Enter your name.
2.  Choose the number of rounds you want to play.
3.  Before the game begins, the leaderboard for your selected number of
    rounds is displayed.
4.  Each round:
    -   Enter a bet amount.
    -   You and the computer roll dice.
    -   The higher roll wins the bet.
5.  If you type `x` when prompted between rounds, the game ends early
    and your score will NOT be recorded.
6.  If your money reaches \$0, you are automatically bankrupt and the
    game ends.

At the end of all rounds (if not quit early or bankrupt), your final
score may be recorded in the leaderboard.

------------------------------------------------------------------------

## Leaderboard System

The game tracks the **Top 3 scores** for each round count separately.

For example: - 1 round → Top 3 scores - 2 rounds → Top 3 scores - 3
rounds → Top 3 scores

Scores are stored in:

    scoreboard.txt

### Design Note About the Scoreboard

The leaderboard was intentionally implemented using a simple text file
for game purposes.\
While it is possible for a user to manually edit `scoreboard.txt`, this
project is a low-security console game designed for fun and learning the kotlin programming language!

Because this is not a high-security application, advanced protections
(such as encryption or database validation) were intentionally not
implemented.

------------------------------------------------------------------------

##  Example Game Output

Below is a simplified example of what gameplay may look like:

    ==============================
    Welcome Alex!
    You will play 2 rounds.
    ==============================

    🏆 Top Scores for 3 Round(s) 🏆
    1. mclovin - $300
    2. nate - $270
    3. bigfoot - $250

    --- ROUND 1 ---
    You bet: $40
    
    🎲 Press Enter to roll your FIRST die...
    
    Your first roll: 1
    
    🤖 Computer rolling first die...
    Computer rolled: 4
    
    🎲 Press Enter to roll your SECOND die...
    
    Your second roll: 6
    
    🤖 Computer rolling second die...
    Computer rolled: 2

    Calculating results...
    ------------------------------
    🎲 Your total: 7
    🤖 Computer total: 6
    ------------------------------
    🏆 You win the round! +$40
    
    Press Enter to continue, or type 'x' to quit:


If the player goes bankrupt:

    💀 You are bankrupt!
    GAME OVER
    Final money: $0

If the player quits early:

    You chose to quit the game early. Scores will not be recorded.

------------------------------------------------------------------------

## Requirements

-   Java (JDK 8 or higher)
-   Kotlin Compiler installed (`kotlinc`)

You can check installation with:

    java -version
    kotlinc -version

------------------------------------------------------------------------

## How to Clone and Run the Project

### 1. Clone the Repository

    git clone https://github.com/mollyoconnorr/KotlinDiceBettingGame.git
    cd KotlinDiceBettingGame

------------------------------------------------------------------------

###  2. Compile the Program

    kotlinc src/KotlinDiceGame.kt 

------------------------------------------------------------------------

###  Run the Program

    kotlin KotlinDiceGameKt

------------------------------------------------------------------------

## Project Structure

    KotlinDiceBettingGame/
    │
    ├── src/
    │   └── KotlinDiceBettingGame.kt
    ├── scoreboard.txt
    ├── .gitignore
    └── README.md

------------------------------------------------------------------------

## References

-   All core game logic, structure, and print statements were written by
    the author.
-   ChatGPT was used for assistance with:
    -   Adding emojis
    -   ANSI color formatting
-   Kotlin Official Documentation: https://kotlinlang.org/docs

------------------------------------------------------------------------

## Author

Created by: Molly O'Connor
Course: CS-495 (Computer Science Seminar) 

------------------------------------------------------------------------

Enjoy the game! 🎲
