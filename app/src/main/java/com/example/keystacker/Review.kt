package com.example.keystacker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Review : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEt: EditText
    private lateinit var submitBtn: Button
    private lateinit var gameImageIv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review) // use your actual layout name

        spinner = findViewById(R.id.gameSpinner)
        ratingBar = findViewById(R.id.ratingBar)
        reviewEt = findViewById(R.id.review)
        submitBtn = findViewById(R.id.button)
        gameImageIv = findViewById(R.id.gameimagerate)

        // 1) Populate spinner (manual list). First item is a prompt.
        val games = listOf(
            "Select a game",
            "Need for Speed",
            "Battlefield 4",
            "Grand Theft Auto V",
            "Far Cry 4"
        )
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            games
        )

        // (Optional) Change preview image when selecting a game
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                val selected = games[position]
                gameImageIv.setImageResource(
                    when (selected) {
                        "Need for Speed" -> R.drawable.nfs
                        "Battlefield 4" -> R.drawable.game3
                        "Grand Theft Auto V" -> R.drawable.gta
                        "Far Cry 4" -> R.drawable.farcry
                        else -> R.drawable.game3                 // default/placeholder
                    }
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        submitBtn.setOnClickListener { handleSubmit(games) }
    }

    private fun handleSubmit(games: List<String>) {
        val selectedIndex = spinner.selectedItemPosition
        val rating = ratingBar.rating
        val text = reviewEt.text.toString().trim()

        // --- Validation ---
        if (selectedIndex <= 0) {
            toast("Please select a game"); return
        }
        // Require at least 1 star (range is 1–5)
        if (rating < 1f) {
            toast("Please give a rating (1–5 stars)"); return
        }
        val words = countWords(text)
        // “less than 50 words” -> max 49 words
        if (words == 0) {
            toast("Please write a short review"); return
        }
        if (words >= 50) {
            toast("Review must be less than 50 words"); return
        }

        // For now we only increment profile-specific review count:
        PurchaseStore.incrementReviewCount(this)

        toast("Thanks for your review!")
        // Optional: clear inputs
        ratingBar.rating = 0f
        reviewEt.setText("")
        spinner.setSelection(0)
    }

    private fun countWords(s: String): Int =
        s.split(Regex("\\s+")).filter { it.isNotBlank() }.size

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}