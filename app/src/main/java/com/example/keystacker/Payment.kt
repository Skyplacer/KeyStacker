package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val Proceed = findViewById<Button>(R.id.Proceed)
        Proceed.setOnClickListener {
            val intent = Intent(this, PaymentSuccess::class.java)
            startActivity(intent)
        }
    }
}