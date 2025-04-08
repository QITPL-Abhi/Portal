package com.android.portal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var btn_sign_up = findViewById<TextView>(R.id.btn_sign_up)
        btn_sign_up.setOnClickListener {
            val navigateToSignup = Intent(this, SignupActivity::class.java)
            startActivity(navigateToSignup)
        }
    }
}