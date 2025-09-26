package com.example.keystacker

import android.content.Context

object PurchaseStore {
    private const val LIB_PREFS = "library_prefs"

    private fun currentEmail(context: Context): String? =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getString("current_email", null)
            ?.trim()
            ?.lowercase()

    fun incrementCount(context: Context) {
        val email = currentEmail(context) ?: return
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        val key = "count_$email"
        val next = (sp.getInt(key, 0) + 1)
        sp.edit().putInt(key, next).apply()
    }

    fun getCount(context: Context): Int {
        val email = currentEmail(context) ?: return 0
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        return sp.getInt("count_$email", 0)
    }

    // Optional: store images per user (if you’re saving purchased covers)
    fun addPurchasedImage(context: Context, imgRes: Int) {
        val email = currentEmail(context) ?: return
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        val key = "imgs_$email"
        val set = sp.getStringSet(key, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        set.add(imgRes.toString())
        sp.edit().putStringSet(key, set).apply()
    }

    fun getPurchasedImages(context: Context): List<Int> {
        val email = currentEmail(context) ?: return emptyList()
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        val set = sp.getStringSet("imgs_$email", emptySet()) ?: emptySet()
        return set.mapNotNull { it.toIntOrNull() }
    }

    fun addPoints(context: Context, points: Int) {
        val email = currentEmail(context) ?: return
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        val key = "points_$email"
        val total = sp.getInt(key, 0) + points.coerceAtLeast(0)
        sp.edit().putInt(key, total).apply()
    }

    fun getPoints(context: Context): Int {
        val email = currentEmail(context) ?: return 0
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        return sp.getInt("points_$email", 0)
    }

    fun incrementReviewCount(context: Context) {
        val email = currentEmail(context) ?: return
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        val key = "reviews_$email"
        val next = sp.getInt(key, 0) + 1
        sp.edit().putInt(key, next).apply()
    }

    fun getReviewCount(context: Context): Int {
        val email = currentEmail(context) ?: return 0
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        return sp.getInt("reviews_$email", 0)
    }

    // --- One-time discount state (percent) ---
    fun getActiveDiscountPercent(context: Context): Int {
        val email = currentEmail(context) ?: return 0
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        return sp.getInt("discount_$email", 0) // 0 = none
    }
    private fun setActiveDiscountPercent(context: Context, percent: Int) {
        val email = currentEmail(context) ?: return
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)
        sp.edit().putInt("discount_$email", percent.coerceIn(0, 100)).apply()
    }
    fun clearActiveDiscount(context: Context) = setActiveDiscountPercent(context, 0)

    /** Try to redeem: spend points for a one-time discount. Returns true on success. */
    fun redeem(context: Context, requiredPoints: Int, discountPercent: Int): Boolean {
        val email = currentEmail(context) ?: return false
        val sp = context.getSharedPreferences(LIB_PREFS, Context.MODE_PRIVATE)

        // already have a pending discount?
        if (getActiveDiscountPercent(context) > 0) return false

        val pointsKey = "points_$email"
        val current = sp.getInt(pointsKey, 0)
        if (current < requiredPoints) return false

        // deduct points and set discount atomically
        sp.edit()
            .putInt(pointsKey, current - requiredPoints)
            .putInt("discount_$email", discountPercent.coerceIn(1, 100))
            .apply()
        return true
    }
}
