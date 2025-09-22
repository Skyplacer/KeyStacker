package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class GamePage1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page1)

        val addtocart = findViewById<Button>(R.id.addtocart)
        addtocart.setOnClickListener {
            val intent = Intent(this, Checkout::class.java)
            startActivity(intent)
        }

        val buynow = findViewById<Button>(R.id.buynow)
        buynow.setOnClickListener {
            val intent = Intent(this, Checkout::class.java)
            startActivity(intent)
        }

        val profilepicgame = findViewById<ImageView>(R.id.profilepicgame)
        profilepicgame.setOnClickListener {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
        }
    }
}