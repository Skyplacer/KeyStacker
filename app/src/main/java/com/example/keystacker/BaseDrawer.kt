package com.example.keystacker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

abstract class BaseDrawer : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    /** Call this in child activities instead of setContentView(...) */
    protected fun setContentLayout(@LayoutRes layoutResId: Int) {
        super.setContentView(R.layout.activity_drawer)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Inflate child layout into content area
        val contentFrame = findViewById<View>(R.id.contentFrame) as android.widget.FrameLayout
        LayoutInflater.from(this).inflate(layoutResId, contentFrame, true)

        // Wire menu item clicks
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            if (this !is Homescreen) {
                startActivity(Intent(this, Homescreen::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            }
            closeDrawer()
        }
        findViewById<TextView>(R.id.navLibrary).setOnClickListener {
            if (this !is Library) {
                startActivity(Intent(this, Library::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            }
            closeDrawer()
        }
        findViewById<TextView>(R.id.navStackpoints).setOnClickListener {
            if (this !is StackPoints) {
                startActivity(Intent(this, StackPoints::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            }
            closeDrawer()
        }
        findViewById<TextView>(R.id.navSettings).setOnClickListener {
            if (this !is Settings) {
                startActivity(Intent(this, Settings::class.java).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
            }
            closeDrawer()
        }

        val logout = findViewById<TextView>(R.id.navLogOut)
        logout.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            closeDrawer()
            toast("Successfully Logged Out")
        }

        // If child layout has a menu button with this ID, hook it automatically
        findViewById<View?>(R.id.menuButton)?.setOnClickListener { toggleDrawer() }
    }

    protected fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)
    protected fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)
    protected fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) closeDrawer() else openDrawer()
    }

    override fun onBackPressed() {
        if (::drawerLayout.isInitialized && drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}