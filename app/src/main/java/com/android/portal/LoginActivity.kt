package com.android.portal

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
                        println("${response.body()?.status.toString()} ${response.body()?.message.toString()}")
                    } else {
                        Log.e("LoginError", "Error: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Exception: ${e.localizedMessage}")
                }
            }

        }

    }
}