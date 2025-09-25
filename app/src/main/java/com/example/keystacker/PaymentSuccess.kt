package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // You can set a layout if you have one. For simplicity, just toast & continue.
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()

        // Go to Library
        startActivity(Intent(this, Library::class.java))
        finish()
    }
}