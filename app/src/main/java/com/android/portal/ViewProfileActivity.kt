package com.android.portal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ViewProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_profile)
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loginUserId = sharedPref.getString("login", "")

        val profileNameId = findViewById<TextView>(R.id.profileNameId)
        val profileEmailId = findViewById<TextView>(R.id.profileEmailId)
        val profileGenderId = findViewById<TextView>(R.id.profileGenderId)
        val profileHobbiesId = findViewById<TextView>(R.id.profileHobbiesId)
        val profileDesignationId = findViewById<TextView>(R.id.profileDesignationId)
        val profileImageViewId = findViewById<ImageView>(R.id.profileImageViewId)



        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getViewData(
                    id = loginUserId ?: ""
                )

                if (response.isSuccessful) {
                    val ViewResponse = response.body()
                    if (ViewResponse?.status.toString() == "success") {
                        profileNameId.text = ViewResponse?.data?.name.toString()
                        profileEmailId.text = ViewResponse?.data?.email.toString()
                        profileGenderId.text = ViewResponse?.data?.gender.toString()
                        profileHobbiesId.text= ViewResponse?.data?.hobbies.toString()
                        profileDesignationId.text= ViewResponse?.data?.designation.toString()
                        Picasso.get()
                            .load("http://192.168.1.17/abhi/app_api/uploads/" + ViewResponse?.data?.profile_image.toString())
                            .into(profileImageViewId)
                        println(ViewResponse?.data?.name.toString())
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@ViewProfileActivity,
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
                        this@ViewProfileActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("DashboardActivity", "Error: ${e.localizedMessage}", e)
                }
            }
        }
    }
}