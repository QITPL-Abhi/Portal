package com.android.portal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val loginShowPassId = findViewById<ImageView>(R.id.loginShowPassId)
        val loginEmailId = findViewById<EditText>(R.id.loginEmailId)
        val passwordLoginID = findViewById<EditText>(R.id.loginPasswordId)
        val signupBtnId = findViewById<Button>(R.id.signupBtnId)
        val navigateToSignupId = findViewById<TextView>(R.id.navigateToSignupId)


        var isPasswordVisible = false

        loginShowPassId.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Show password
                passwordLoginID.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                loginShowPassId.setImageResource(R.drawable.eyes_open) // Replace with your open eye drawable
            } else {
                // Hide password
                passwordLoginID.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                loginShowPassId.setImageResource(R.drawable.close_eyes) // Replace with your closed eye drawable
            }

            // Move cursor to the end
            passwordLoginID.setSelection(passwordLoginID.text.length)
        }

        signupBtnId.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.getlogin(
                        email = loginEmailId.text.toString(),
                        password = passwordLoginID.text.toString()
                    )

                    if (response.isSuccessful) {
                        println("login respose " + response.body()?.data.toString())
                        if (response.body()?.status.toString() == "success") {
                            val session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            session.edit() {
                                putString("login", response.body()?.data.toString())
                            }
                            val navigateToDashboard =
                                Intent(this@LoginActivity, DashboardActivity::class.java)
                            startActivity(navigateToDashboard)
                            finish()
                        }
                    } else {
                        Log.e("LoginError", "Error: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Exception: ${e.localizedMessage}")
                }
            }

        }

        navigateToSignupId.setOnClickListener {
            val navigateToSignup = Intent(this, SignupActivity::class.java)
            startActivity(navigateToSignup)
        }

    }
}