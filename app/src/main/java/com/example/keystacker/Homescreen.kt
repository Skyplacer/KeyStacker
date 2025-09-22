package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class Homescreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        val profileHome = findViewById<ImageView>(R.id.profilepicHome)
        profileHome.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val game1 = findViewById<ImageView>(R.id.game1)
        game1.setOnClickListener {
            val intent = Intent(this, GamePage1::class.java)
            startActivity(intent)
        }
    }
}