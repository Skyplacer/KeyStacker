package com.example.keystacker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Library : BaseDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_library)
    }
}