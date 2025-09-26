package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {

    private lateinit var etCardNo: EditText
    private lateinit var etMMYY: EditText
    private lateinit var etCVV: EditText
    private lateinit var etName: EditText
    private lateinit var etZip: EditText
    private lateinit var spRegion: Spinner
    private lateinit var btnProceed: Button

    private var purchaseImgRes: Int = 0
    private var purchaseName: String = "Game"
    private var purchasePrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment) // your layout name

        etCardNo = findViewById(R.id.cardno)
        etMMYY   = findViewById(R.id.monthyear)
        etCVV    = findViewById(R.id.cvv)
        etName   = findViewById(R.id.cardowner)
        etZip    = findViewById(R.id.zipcode)
        spRegion = findViewById(R.id.region)
        btnProceed = findViewById(R.id.Proceed)

        // Spinner options
        val regions = listOf("Select Region", "Asia", "Europe", "NA", "SA", "Africa")
        spRegion.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regions)

        // Read what we’re buying
        purchaseImgRes = intent.getIntExtra("purchase_img_res", 0)
        purchaseName = intent.getStringExtra("purchase_name") ?: "Game"
        purchasePrice = intent.getDoubleExtra("purchase_price", 0.0)

        btnProceed.setOnClickListener {
            handleProceed()
            PurchaseStore.clearActiveDiscount(this)
            val i = Intent(this, PaymentSuccess::class.java).apply {
                putExtra("purchase_name", purchaseName)
                putExtra("purchase_price", purchasePrice)
                putExtra("purchase_img_res", purchaseImgRes)
            }
            startActivity(i)
            finish()
        }
    }

    private fun handleProceed() {
        val cardNo = etCardNo.text.toString().replace(" ", "")
        val mmyy = etMMYY.text.toString().trim()
        val cvv = etCVV.text.toString().trim()
        val name = etName.text.toString().trim()
        val zip = etZip.text.toString().trim()
        val regionIdx = spRegion.selectedItemPosition

        // --- Validations (simple & clear) ---
        if (cardNo.length !in 13..19 || !cardNo.all { it.isDigit() }) {
            toast("Enter a valid card number"); return
        }
        if (!isValidMMYY(mmyy)) {
            toast("Enter a valid expiry (MM/YY)"); return
        }
        if (cvv.length !in 3..4 || !cvv.all { it.isDigit() }) {
            toast("Enter a valid CVV"); return
        }
        if (name.isEmpty()) {
            toast("Enter cardholder name"); return
        }
        if (zip.isEmpty()) {
            toast("Enter ZIP/Postal code"); return
        }
        if (regionIdx <= 0) {
            toast("Please select a region"); return
        }

        // --- “Process” payment (mock) ---
        // Save purchase image to SharedPreferences so Library can show it
        if (purchaseImgRes != 0) {
            PurchaseStore.addPurchasedImage(this, purchaseImgRes)
        }
        PurchaseStore.incrementCount(this)
        val points = intent.getIntExtra("purchase_points", 0)
        PurchaseStore.addPoints(this, points)
    }

    private fun isValidMMYY(mmyy: String): Boolean {
        // basic MM/YY check
        if (!mmyy.matches(Regex("""^\d{2}/\d{2}$"""))) return false
        val (mmStr, yyStr) = mmyy.split("/")
        val mm = mmStr.toIntOrNull() ?: return false
        if (mm !in 1..12) return false
        // (Optional) add expiry date in the future check
        return true
    }

    private fun savePurchasedImage(imgRes: Int) {
        val sp = getSharedPreferences("library_prefs", MODE_PRIVATE)
        val set = sp.getStringSet("purchased_imgs", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        set.add(imgRes.toString())
        sp.edit().putStringSet("purchased_imgs", set).apply()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}