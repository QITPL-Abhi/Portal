package com.android.portal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigateToLogin = Intent(this, LoginActivity::class.java)
        startActivity(navigateToLogin)
    }
}