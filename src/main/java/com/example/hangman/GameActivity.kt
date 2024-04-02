package com.example.hangman

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var wordTextView: TextView
    private lateinit var letterButtonsLayout: GridLayout

    private val hangmanImages = arrayOf(
        R.drawable.game0,
        R.drawable.game1,
        R.drawable.game2,
        R.drawable.game3,
        R.drawable.game4,
        R.drawable.game5,
        R.drawable.game6,
        R.drawable.game7
    )

    private val wordHints = mapOf(
        "Afghanistan" to "Capital: Kabul",
        "Argentina" to "Capital: Buenos Aires",
        "Australia" to "Capital: Canberra",
        "Belgium" to "Capital: Brussels",
        "Brazil" to "Capital: Brasília",
        "China" to "Capital: Beijing",
        "Croatia" to "Capital: Zagreb",
        "Denmark" to "Capital: Copenhagen",
        "Egypt" to "Capital: Cairo",
        "Estonia" to "Capital: Tallinn",
        "Finland" to "Capital: Helsinki",
        "France" to "Capital: Paris",
        "Greece" to "Capital: Athens",
        "Iceland" to "Capital: Reykjavík",
        "India" to "Capital: New Delhi",
        "Iran" to "Capital: Tehran",
        "Ireland" to "Capital: Dublin",
        "Japan" to "Capital: Tokyo",
        "Latvia" to "Capital: Riga",
        "Libya" to "Capital: Tripoli",
        "Luxembourg" to "Capital: Luxembourg City",
        "Malta" to "Capital: Valletta",
        "Mexico" to "Capital: Mexico City",
        "Morocco" to "Capital: Rabat",
        "Netherlands" to "Capital: Amsterdam",
        "Norway" to "Capital: Oslo",
        "Pakistan" to "Capital: Islamabad",
        "Poland" to "Capital: Warsaw",
        "Portugal" to "Capital: Lisbon",
        "Russia" to "Capital: Moscow",
        "Slovakia" to "Capital: Bratislava",
        "Somalia" to "Capital: Mogadishu",
        "Sweden" to "Capital: Stockholm",
        "Switzerland" to "Capital: Bern",
        "Thailand" to "Capital: Bangkok",
        "Turkey" to "Capital: Ankara",
        "Ukraine" to "Capital: Kiev",
        "Vietnam" to "Capital: Hanoi"
    )

    private var failedAttempts = 0
    private var wordToGuess = ""
    private var displayWord = ""
    private var guessedLetters = mutableListOf<Char>()
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        imageView = findViewById(R.id.imageViewHangman)
        wordTextView = findViewById(R.id.textViewWord)
        letterButtonsLayout = findViewById(R.id.letterButtonsLayout)
        findViewById<Button>(R.id.buttonGuess).setOnClickListener {
            displayHint()
        }

        findViewById<Button>(R.id.buttonReset).setOnClickListener {
            resetGame()
        }
        addLetterButtons()
        resetGame()
    }

    private fun chooseRandomWord() {
        val wordList = listOf(
            "Afghanistan", "Argentina", "Australia", "Belgium", "Brazil", "China", "Croatia", "Denmark",
            "Egypt", "Estonia", "Finland", "France", "Greece", "Iceland", "India", "Iran", "Ireland",
            "Japan", "Latvia", "Libya", "Luxembourg", "Malta", "Mexico", "Morocco", "Netherlands",
            "Norway", "Pakistan", "Poland", "Portugal", "Russia", "Slovakia", "Somalia", "Sweden",
            "Switzerland", "Thailand", "Turkey", "Ukraine", "Vietnam"
        )
        wordToGuess = wordList.random()
        Log.d("WordToGuess", "Word to guess: $wordToGuess")
        initializeDisplayWord()
        displayWord = "_".repeat(wordToGuess.length)
        wordTextView.text = displayWord
    }

    private fun displayHint() {
        val hint = wordHints[wordToGuess]
        hint?.let {
            wordTextView.text = it
        }
    }

    private fun initializeDisplayWord() {
        displayWord = "_".repeat(wordToGuess.length)
    }

    private fun gameOver() {
        findViewById<Button>(R.id.buttonGuess).isEnabled = false // Disable guessing
    }

    private fun resetGame() {
        failedAttempts = 0
        guessedLetters.clear()
        wordToGuess = ""
        chooseRandomWord()
        displayWord = "_".repeat(wordToGuess.length)
        wordTextView.text = displayWord
        updateImage()
        findViewById<Button>(R.id.buttonGuess).isEnabled = true
        findViewById<TextView>(R.id.textViewScore).text = "Score: $score"

        val gridLayout = findViewById<GridLayout>(R.id.letterButtonsLayout)
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is Button) {
                child.visibility = Button.VISIBLE
            }
        }
    }



    private fun handleGuess(guess: Char) {
        if (guess !in guessedLetters) {
            if (guess in wordToGuess) {
                guessedLetters.add(guess)
                updateDisplayWord()
            } else {
                failedAttempts++
                updateImage()
            }
        }

        if (displayWord == wordToGuess) {
            gameOver()
            wordTextView.text = "Congratulations! You guessed the word!"
        } else if (failedAttempts >= hangmanImages.size) {
            gameOver()
            wordTextView.text = "Game over! The word was: $wordToGuess"
        }
    }


    private fun updateDisplayWord() {
        val wordToShow = StringBuilder()
        for (char in wordToGuess) {
            if (guessedLetters.contains(char)) {
                wordToShow.append(char)
            } else {
                wordToShow.append("_")
            }
        }
        displayWord = wordToShow.toString()
        wordTextView.text = displayWord
    }



    private fun updateImage() {
        if (failedAttempts < hangmanImages.size) {
            imageView.setImageResource(hangmanImages[failedAttempts])
        }
    }

    private fun addLetterButtons() {
        val alphabet = ('a'..'z').toList()
        val gridLayout = findViewById<GridLayout>(R.id.letterButtonsLayout)


        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels


        val buttonWidth = screenWidth / 10

        for ((index, letter) in alphabet.withIndex()) {
            val button = Button(this)
            button.text = letter.toString()


            val params = GridLayout.LayoutParams().apply {
                width = buttonWidth
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(8, 8, 8, 8) // Adjust margins as needed
            }

            button.layoutParams = params


            button.textSize = 18f

            button.setOnClickListener {
                handleGuess(letter)
                button.visibility = Button.GONE
            }
            gridLayout.addView(button)
        }
    }
}
