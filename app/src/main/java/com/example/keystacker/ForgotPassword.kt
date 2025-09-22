package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val forgotPass = findViewById<Button>(R.id.changepass)
        forgotPass.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }


}