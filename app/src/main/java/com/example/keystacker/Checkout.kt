package com.example.keystacker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import java.text.NumberFormat
import java.util.Locale

class Checkout : AppCompatActivity() {

    private val currency = NumberFormat.getCurrencyInstance(Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val pay = findViewById<Button>(R.id.Pay)
        pay.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }

        val tvGameName: TextView = findViewById(R.id.gamename)
        val tvGamePrice: TextView = findViewById(R.id.gamevalue)
        val ivGame: ImageView = findViewById(R.id.imageView7)

        val tvAmountLabel: TextView = findViewById(R.id.amount)          // "Amount" (label)
        val tvAmountValue: TextView = findViewById(R.id.amountvalue)
        val tvDiscountValue: TextView = findViewById(R.id.discountvalue)
        val tvTotalValue: TextView = findViewById(R.id.totalvalue)
        val tvStackPoints: TextView = findViewById(R.id.stackpointvalue)

        // Read cart extras
        val name  = intent.getStringExtra("cart_game_name") ?: "Unknown Game"
        val img   = intent.getIntExtra("cart_game_img_res", 0)
        val price = intent.getDoubleExtra("cart_game_price", 0.0)
        val qty   = intent.getIntExtra("cart_qty", 1)

        // Show cart row (single item)
        tvGameName.text = name
        if (img != 0) ivGame.setImageResource(img)

        val lineSubtotal = price * qty
        val discount = 0.0  // plug your discount logic here
        val total = lineSubtotal - discount

        tvGamePrice.text = currency.format(price)           // right side of the row
        tvAmountValue.text = currency.format(lineSubtotal)  // subtotal
        tvDiscountValue.text = currency.format(discount)
        tvTotalValue.text = currency.format(total)

        // Optional: stack points (example: 1 point per $ spent)
        tvStackPoints.text = (total).toInt().toString()
    }
}