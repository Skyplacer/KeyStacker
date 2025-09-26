package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Library::class.java))
            finish()
        }, 3000)
    }
}