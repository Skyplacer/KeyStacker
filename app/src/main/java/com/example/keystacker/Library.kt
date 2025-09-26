package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Library : BaseDrawer() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_library) // your layout name

        val iv1: ImageView = findViewById(R.id.gamepurchase1)
        val iv2: ImageView = findViewById(R.id.gamepurchase2)
        val iv3: ImageView = findViewById(R.id.gamepurchase3)
        val iv4: ImageView = findViewById(R.id.gamepurchase4)

        val purchased = loadPurchasedImages()
        val imageViews = listOf(iv1, iv2, iv3, iv4)

        // Hide all first (optional)
        imageViews.forEach { it.setImageDrawable(null) }

        // Fill sequentially
        purchased.take(imageViews.size).forEachIndexed { index, resId ->
            imageViews[index].setImageResource(resId)
        }

        val profileHome = findViewById<ImageView>(R.id.profilepic1)
        profileHome.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    private fun loadPurchasedImages(): List<Int> {
        val sp = getSharedPreferences("library_prefs", MODE_PRIVATE)
        val set = sp.getStringSet("purchased_imgs", emptySet()) ?: emptySet()
        return set.mapNotNull { it.toIntOrNull() }
    }
}

