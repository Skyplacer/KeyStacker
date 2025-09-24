package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast

class ForgotPassword : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etCode: EditText
    private lateinit var etNewPass: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var btnSendCode: Button
    private lateinit var btnChange: Button

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.passwordNew)
        etCode = findViewById(R.id.EmailReset)
        etNewPass = findViewById(R.id.EmailConfirm)
        etConfirmPass = findViewById(R.id.PasswordConfirm)
        btnSendCode = findViewById(R.id.sendemailcode)
        btnChange = findViewById(R.id.changepass)

        repo = UserRepository(UserDBHelper(this))

        btnSendCode.setOnClickListener { onSendCode() }
        btnChange.setOnClickListener {
            onChangePassword()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
    private fun onSendCode() {
        val email = etEmail.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Enter a valid email")
            return
        }
        if (!repo.emailExists(email)) {
            toast("No account found for this email")
            return
        }

        val code = ResetCode.generateAndSave(this, email, minutesValid = 5)
        // In a real app, email the code. For now, show it to the user (DEV ONLY).
        toast("Code sent. (DEV: $code)")
    }

    private fun onChangePassword() {
        val email = etEmail.text.toString().trim()
        val code = etCode.text.toString().trim()
        val newP = etNewPass.text.toString()
        val conf = etConfirmPass.text.toString()

        // Required fields checks -> show specific errors as you asked
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Enter a valid email"); return
        }
        if (code.length != 6) {
            toast("Enter the 6-digit email code"); return
        }
        if (newP.isEmpty() || conf.isEmpty()) {
            toast("Enter new and confirm password"); return
        }
        if (newP != conf) {
            toast("Passwords do not match"); return
        }
        if (newP.length < 6) {
            toast("Password must be at least 6 characters"); return
        }
        if (!repo.emailExists(email)) {
            toast("No account found for this email"); return
        }
        if (!ResetCode.verify(this, email, code)) {
            toast("Invalid or expired code"); return
        }

        val ok = repo.setPasswordByEmail(email, newP)
        if (ok) {
            ResetCode.clear(this, email)
            toast("Password changed. You can log in with your new password.")
            // Optional: finish() to go back to Login if this screen was opened from Login
            // finish()
        } else {
            toast("Failed to change password")
        }
    }
    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}