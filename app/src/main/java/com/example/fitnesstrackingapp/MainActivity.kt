package com.example.fitnesstrackingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startTrackingButton: Button = findViewById(R.id.btnStartTracking)
        startTrackingButton.setOnClickListener {
            val intent = Intent(this, TrackingActivity::class.java)
            startActivity(intent)
        }
    }
}