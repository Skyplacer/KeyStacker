package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Profile : AppCompatActivity() {
    private lateinit var tvUsername: TextView
    private lateinit var repo: UserRepository
    private lateinit var etCurrent: EditText
    private lateinit var etNew: EditText
    private lateinit var btnChange: Button
    private lateinit var btnDelete: Button
    private lateinit var tvGamesPurchased: TextView
    private lateinit var tvPoints: TextView
    private lateinit var tvReviewCount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvUsername = findViewById(R.id.UsernameProfile)
        etCurrent  = findViewById(R.id.currentPass)
        etNew      = findViewById(R.id.newPass)
        btnChange  = findViewById(R.id.changePassButton)
        btnDelete = findViewById(R.id.deleteAccountButton)
        repo = UserRepository(UserDBHelper(this))

        tvGamesPurchased = findViewById(R.id.gamespurchasedvalue)
        updatePurchasedCount()

        tvPoints = findViewById(R.id.stackpointvalue2)
        updatePoints()

        tvReviewCount = findViewById(R.id.reviewcountvalue)
        updateReviewCount()

        // 1) Get the current user's email from SharedPreferences
        val email = getSharedPreferences("auth_prefs", MODE_PRIVATE)
            .getString("current_email", null)

        if (email.isNullOrBlank()) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnChange.setOnClickListener {
            changePassword(email)
        }

        btnDelete.setOnClickListener {
            confirmDelete(email)
        }

        // 2) Load display name from DB and set it
        val user = repo.getUserByEmail(email)
        if (user != null) {
            tvUsername.text = user.displayName
        } else {
            tvUsername.text = "Guest"
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        updatePurchasedCount()
        updatePoints()
        updateReviewCount()
    }

    private fun updatePurchasedCount(){
        tvGamesPurchased.text = PurchaseStore.getCount(this).toString()
    }

    private fun updatePoints() {
        tvPoints.text = PurchaseStore.getPoints(this).toString()
    }

    private fun updateReviewCount() {
        tvReviewCount.text = PurchaseStore.getReviewCount(this).toString()
    }

    private fun changePassword(email: String) {
        val current = etCurrent.text.toString()
        val newPass = etNew.text.toString()

        // Basic validation rules — adjust to your policy
        if (current.isEmpty()) { toast("Enter your current password"); return }
        if (newPass.length < 6) { toast("New password must be at least 6 characters"); return }
        if (newPass == current) { toast("New password must be different"); return }

        val ok = repo.changePasswordByEmail(email, current, newPass)
        if (ok) {
            toast("Password changed")
            etCurrent.text?.clear()
            etNew.text?.clear()
        } else {
            toast("Current password is incorrect")
        }
    }

    private fun confirmDelete(email: String) {
        val currentPwd = etCurrent.text.toString()   // reuse your Current Password field
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("This will permanently delete your account. Continue?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                performDelete(email, currentPwd)
            }
            .show()
    }

    private fun performDelete(email: String, currentPwd: String) {
        // RECOMMENDED: require current password
        if (currentPwd.isEmpty()) {
            toast("Enter your current password to delete"); return
        }
        val deleted = repo.deleteByEmailWithPassword(email, currentPwd)

        // If you prefer simple delete (no password), replace the two lines above with:
        // val deleted = repo.deleteByEmail(email)

        if (deleted) {
            // Clear session
            getSharedPreferences("auth_prefs", MODE_PRIVATE).edit()
                .remove("current_email")
                .apply()
            toast("Account deleted")

            // Navigate back to Login and clear back stack
            val i = Intent(this, Login::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(i)
            finish()
        } else {
            toast("Delete failed (wrong password or account missing)")
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}