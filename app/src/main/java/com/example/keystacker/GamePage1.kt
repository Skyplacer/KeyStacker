package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class GamePage1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page1)

        val tvTitle: TextView = findViewById(R.id.textView3)
        val ivThumb: ImageView = findViewById(R.id.gamethumbnail)
        val tvPrice: TextView = findViewById(R.id.textView4)
        val btnAdd: Button = findViewById(R.id.addtocart)

        val gameName = tvTitle.text.toString()
        val gameImageRes = R.drawable.game3    // keep in sync with your XML
        val gamePrice = 4.99

        btnAdd.setOnClickListener {
            val i = Intent(this, Checkout::class.java).apply {
                putExtra("cart_game_name", gameName)
                putExtra("cart_game_img_res", gameImageRes)
                putExtra("cart_game_price", gamePrice)
                putExtra("cart_qty", 1)
            }
            startActivity(i)
        }

        val buynow = findViewById<Button>(R.id.buynow)
        buynow.setOnClickListener {
            val intent = Intent(this, Checkout::class.java).apply{
                putExtra("cart_game_name", gameName)
                putExtra("cart_game_img_res", gameImageRes)
                putExtra("cart_game_price", gamePrice)
                putExtra("cart_qty", 1)
            }
            startActivity(intent)
        }

        val profilepicgame = findViewById<ImageView>(R.id.profilepicgame)
        profilepicgame.setOnClickListener {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
        }
    }
}