package com.android.portal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigateToDashboard = Intent(this, DashboardActivity::class.java)
        startActivity(navigateToDashboard)
    }
}