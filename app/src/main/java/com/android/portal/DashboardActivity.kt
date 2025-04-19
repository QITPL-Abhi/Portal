package com.android.portal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashbaord)
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loginUserId = sharedPref.getString("login", "")
        val userNameDashboardId = findViewById<TextView>(R.id.userNameDashboardId)
        val DashboardProfileImageId = findViewById<ImageView>(R.id.DashboardProfileImageId)
        println("login Id $loginUserId")
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getDashboardData(
                    id = loginUserId ?: ""
                )

                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse?.status.toString() == "success") {
                        userNameDashboardId.text = signupResponse?.data?.name.toString()
                        Picasso.get()
                            .load("http://192.168.1.6/abhi/app_api/uploads/"+signupResponse?.data?.profile_image.toString())
                            .into(DashboardProfileImageId)
                    }
    println(signupResponse?.data?.profile_image.toString())
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Data Retrieve Failed: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(
                            "DashboardActivity",
                            "Data Fetch Failed: ${response.errorBody()?.string()}"
                        )
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("DashboardActivity", "Error: ${e.localizedMessage}", e)
                }
            }
        }


        val logoutbtnId = findViewById<TextView>(R.id.logoutbtnId)


        logoutbtnId.setOnClickListener() {
            val session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            session.edit() {
                putString("login", "")
            }
            val navigateToLogin = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(navigateToLogin)
            finish()
        }
    }
}