package com.example.keystacker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StackPoints : BaseDrawer() {
    private lateinit var tvPoints: TextView
    private lateinit var btn5: Button
    private lateinit var btn10: Button
    private lateinit var btn20: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_points) // <-- use your layout file name

        tvPoints = findViewById(R.id.stackpointvalue3)
        btn5 = findViewById(R.id.point1button)  // 1000 pts -> 5%
        btn10 = findViewById(R.id.point2button) // 2500 pts -> 10%
        btn20 = findViewById(R.id.point3button) // 5000 pts -> 20%

        refreshUI()

        btn5.setOnClickListener { tryRedeem(1000, 5) }
        btn10.setOnClickListener { tryRedeem(2500, 10) }
        btn20.setOnClickListener { tryRedeem(5000, 20) }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        val points = PurchaseStore.getPoints(this)
        tvPoints.text = points.toString()

        val hasPending = PurchaseStore.getActiveDiscountPercent(this) > 0
        // Enable buttons based on balance and no pending discount
        btn5.isEnabled = !hasPending && points >= 1000
        btn10.isEnabled = !hasPending && points >= 2500
        btn20.isEnabled = !hasPending && points >= 5000
    }

    private fun tryRedeem(required: Int, percent: Int) {
        val ok = PurchaseStore.redeem(this, required, percent)
        if (ok) {
            Toast.makeText(this, "Redeemed $percent% off for your next purchase.", Toast.LENGTH_SHORT).show()
            refreshUI()
        } else {
            val msg = if (PurchaseStore.getActiveDiscountPercent(this) > 0)
                "You already have a pending discount. Use it on your next checkout."
            else
                "Not enough points."
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}