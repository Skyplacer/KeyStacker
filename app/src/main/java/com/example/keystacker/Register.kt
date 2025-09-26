package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etDisplayName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirm: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.Email)
        etDisplayName = findViewById(R.id.Username)
        etPassword = findViewById(R.id.Password)
        etConfirm = findViewById(R.id.ConfirmPass)
        cbTerms = findViewById(R.id.checkBox)
        btnRegister = findViewById(R.id.Register)

        repo = UserRepository(UserDBHelper(this))

        btnRegister.setOnClickListener {
            handleCreateUser()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun handleCreateUser(){
        val email = etEmail.text.toString().trim()
        val displayName = etDisplayName.text.toString().trim()
        val password = etPassword.text.toString()
        val confirm = etConfirm.text.toString()

        // Basic validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Please enter a valid email"); return
        }
        if (displayName.isEmpty()) {
            toast("Please enter a display name"); return
        }
        if (password.length < 6) {
            toast("Password must be at least 6 characters"); return
        }
        if (password != confirm) {
            toast("Passwords do not match"); return
        }
        if (!cbTerms.isChecked) {
            toast("You must agree to the terms and conditions"); return
        }

        // Hash password before storing
        val hash = Hashing.sha256(password)

        val rowId = repo.createUser(email, displayName, hash)
        if (rowId > 0) {
            toast("Account created!")
            // TODO: navigate to next screen if needed
            clearFields()
        } else {
            toast("Email already registered")
        }
    }

    private fun clearFields(){
        etPassword.text?.clear()
        etConfirm.text?.clear()
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}