package com.android.portal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileEditActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loginUserId = sharedPref.getString("login", "")
        val editUploadPicBtn = findViewById<ImageButton>(R.id.editUploadPicBtn)
        val editNameId = findViewById<EditText>(R.id.editNameId)
        val editEmailId = findViewById<EditText>(R.id.editEmailId)
        val editPasswordId = findViewById<EditText>(R.id.editPasswordId)
        val editGenderMaleId = findViewById<RadioButton>(R.id.editGenderMaleId)
        val editGenderFemaleId = findViewById<RadioButton>(R.id.editGenderFemaleId)
        val editHobbyCookingId = findViewById<CheckBox>(R.id.editHobbyCookingId)
        val editHobbyCodingId = findViewById<CheckBox>(R.id.editHobbyCodingId)
        val editHobbyGamingId = findViewById<CheckBox>(R.id.editHobbyGamingId)
        val editDesignationSpinnerId = findViewById<Spinner>(R.id.editDesignationSpinnerId)
        val editProfileShowPassId = findViewById<ImageView>(R.id.editProfileShowPassId)

        var isPasswordVisible = false
        editProfileShowPassId.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Show password
                editPasswordId.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                editProfileShowPassId.setImageResource(R.drawable.eyes_open) // Replace with your open eye drawable
            } else {
                // Hide password
                editPasswordId.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editProfileShowPassId.setImageResource(R.drawable.close_eyes) // Replace with your closed eye drawable
            }

            // Move cursor to the end
            editPasswordId.setSelection(editPasswordId.text.length)
        }

        val designations = arrayOf(
            "--Select--",
            "Backend Developer",
            "App Developer",
            "FullStack Web Developer",
            "Front End Developer"
        )

        val adapter = object :
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, designations) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = layoutInflater.inflate(R.layout.custom_spinner_item, parent, false)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = getItem(position)
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primary_text_color
                    )
                ) // Use the theme-aware color
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.white
                    )
                ) // Keep white background for dropdown
                return view
            }
        }
        editDesignationSpinnerId.adapter = adapter


        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getViewData(
                    id = loginUserId ?: ""
                )

                if (response.isSuccessful) {
                    val ViewResponse = response.body()
                    if (ViewResponse?.status.toString() == "success") {
                        editNameId.setText(ViewResponse?.data?.name.toString())
                        editEmailId.setText(ViewResponse?.data?.email.toString())
                        editPasswordId.setText(ViewResponse?.data?.password.toString())
                        if (ViewResponse?.data?.gender.toString() == "Male") {
                            editGenderMaleId.isChecked = true
                        }
                        if (ViewResponse?.data?.gender.toString() == "Female") {
                            editGenderFemaleId.isChecked = true
                        }

                        val hobbiesList =
                            ViewResponse?.data?.hobbies?.split(",")?.map { it.trim().lowercase() }
                                ?: emptyList()
                        println(hobbiesList)
                        if ("cooking" in hobbiesList) {
                            editHobbyCookingId.isChecked = true
                        }
                        if ("coding" in hobbiesList) {
                            editHobbyCodingId.isChecked = true
                        }
                        if ("gaming" in hobbiesList) {
                            editHobbyGamingId.isChecked = true
                        }
                        println(ViewResponse?.data?.designation.toString())
                        Picasso.get()
                            .load("http://192.168.1.4/abhi/portal_apis/uploads/" + ViewResponse?.data?.profile_image.toString())
                            .into(editUploadPicBtn)
                        val designationFromResponse = ViewResponse?.data?.designation.toString()
                        val index = designations.indexOf(designationFromResponse)
                        println(index)
                        if (index >= 0) {
                            editDesignationSpinnerId.setSelection(index)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@ProfileEditActivity,
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
                        this@ProfileEditActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("DashboardActivity", "Error: ${e.localizedMessage}", e)
                }
            }
        }
    }
}