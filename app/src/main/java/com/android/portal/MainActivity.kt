package com.android.portal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loginUserId = sharedPref.getString("login", "")
        println("login id $loginUserId")
        if (loginUserId == "null" || loginUserId == "") {
            val navigateToLogin = Intent(this, LoginActivity::class.java)
            startActivity(navigateToLogin)
            finish()
        } else {
            val navigateToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(navigateToDashboard)
            finish()
        }


    }
}

