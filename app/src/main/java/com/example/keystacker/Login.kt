package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCreate: Button
    private lateinit var btnForgot: Button
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.Email)
        etPass  = findViewById(R.id.Password)
        btnLogin = findViewById(R.id.Login)
        btnCreate = findViewById(R.id.NewAccount)
        btnForgot = findViewById(R.id.ForgotPass)

        repo = UserRepository(UserDBHelper(this))

        btnLogin.setOnClickListener { doLogin() }
        btnCreate.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        val forgotpass = findViewById<Button>(R.id.ForgotPass)
        forgotpass.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        val newacc = findViewById<Button>(R.id.NewAccount)
        newacc.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
    private fun doLogin() {
        val email = etEmail.text.toString().trim()
        val pass  = etPass.text.toString()

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Enter a valid email"); return
        }
        if (pass.isEmpty()) { toast("Enter your password"); return }

        if (repo.verifyByEmail(email, pass)) {
            toast("Welcome!")
            getSharedPreferences("auth_prefs", MODE_PRIVATE)
                .edit()
                .putString("current_email", email.trim().lowercase())
                .apply()
            startActivity(Intent(this, Homescreen::class.java)) // your home activity
            finish()
        } else {
            toast("Invalid email or password")
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}