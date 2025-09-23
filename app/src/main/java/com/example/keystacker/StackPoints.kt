package com.example.keystacker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StackPoints : BaseDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_stack_points)
    }
}