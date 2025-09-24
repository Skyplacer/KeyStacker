package com.example.keystacker

import android.content.Context
import kotlin.random.Random

object ResetCode {
    private const val PREF = "reset_codes"

    fun generateAndSave(context: Context, email: String, minutesValid: Int = 5): String {
        val code = "%06d".format(Random.nextInt(0, 1_000_000))
        val expiry = System.currentTimeMillis() + minutesValid * 60_000L
        val e = email.trim().lowercase()
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putString("${e}_code", code)
            .putLong("${e}_exp", expiry)
            .apply()
        return code
    }

    fun verify(context: Context, email: String, code: String): Boolean {
        val e = email.trim().lowercase()
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val saved = sp.getString("${e}_code", null)
        val exp = sp.getLong("${e}_exp", 0L)
        val now = System.currentTimeMillis()
        return saved != null && saved == code && now <= exp
    }

    fun clear(context: Context, email: String) {
        val e = email.trim().lowercase()
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .remove("${e}_code")
            .remove("${e}_exp")
            .apply()
    }
}