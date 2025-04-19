package com.android.portal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SignupActivity : AppCompatActivity() {

    private lateinit var signupNameId: EditText // Add this
    private lateinit var designationSpinner: Spinner
    private lateinit var uploadProfilePicBtn: Button
    private lateinit var selectedImageView: ImageView
    private lateinit var createAccountBtnId: Button
    private lateinit var signupEmailId: EditText
    private lateinit var signupPasswordId: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var hobbyCooking: CheckBox
    private lateinit var hobbyCoding: CheckBox
    private lateinit var hobbyGaming: CheckBox
    private lateinit var navigateToLoginId: TextView

    private val IMAGE_PICK_CODE = 1000
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        // Initialize Views
        signupNameId = findViewById(R.id.signupNameId)
        designationSpinner = findViewById(R.id.designationSpinner)
        uploadProfilePicBtn = findViewById(R.id.uploadProfilePicBtn)
        selectedImageView = findViewById(R.id.selectedImageView)
        createAccountBtnId = findViewById(R.id.createAccountBtnId)
        signupEmailId = findViewById(R.id.signupEmailId)
        signupPasswordId = findViewById(R.id.signupPasswordId)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        hobbyCooking = findViewById(R.id.hobbyCooking)
        hobbyCoding = findViewById(R.id.hobbyCoding)
        hobbyGaming = findViewById(R.id.hobbyGaming)
        navigateToLoginId = findViewById(R.id.navigateToLoginId)

        navigateToLoginId.setOnClickListener() {
            val navigateToLogin = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(navigateToLogin)
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
        designationSpinner.adapter = adapter

        // Handle Profile Picture Upload
        uploadProfilePicBtn.setOnClickListener {
            pickImageFromGallery()
        }

        // Handle Create Account Button Click
        createAccountBtnId.setOnClickListener {
            signUpUser()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            if (imageUri != null) {
                selectedImageView.setImageURI(imageUri)
                selectedImageView.visibility = ImageView.VISIBLE
                Toast.makeText(this, "Profile picture selected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpUser() {
        val name = signupNameId.text.toString()
        val email = signupEmailId.text.toString()
        val password = signupPasswordId.text.toString()
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        val genderRadioButton = findViewById<RadioButton>(selectedGenderId)
        val gender = genderRadioButton?.text?.toString() ?: ""
        val designation = designationSpinner.selectedItem.toString()

        val selectedHobbies = mutableListOf<String>()
        if (hobbyCooking.isChecked) {
            selectedHobbies.add("cooking")
        }
        if (hobbyCoding.isChecked) {
            selectedHobbies.add("coding")
        }
        if (hobbyGaming.isChecked) {
            selectedHobbies.add("gaming")
        }
        val hobbies = selectedHobbies.joinToString(",")

        val isProfileSelected = imageUri != null

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty() || designation == "--Select--" || hobbies.isEmpty() || !isProfileSelected) {
            Toast.makeText(
                this,
                "Please fill in all the required fields and select a profile picture",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val profilePart: MultipartBody.Part? = imageUri?.let { uri ->
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        val mimeType = contentResolver.getType(uri)
                        val fileExtension = when (mimeType) {
                            "image/png" -> ".png"
                            "image/jpeg" -> ".jpg"  // use .jpg for both jpeg/jpg MIME
                            else -> ".jpg" // fallback
                        }

                        // Create a temp file with correct extension
                        val file = File.createTempFile("profile_image", fileExtension, cacheDir)

                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }

                        val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())

                        // Optional: use dynamic filename
                        val fileName = "profile_image${System.currentTimeMillis()}$fileExtension"

                        MultipartBody.Part.createFormData("profile_image", fileName, requestFile)
                    }
                }

                val response = RetrofitClient.apiService.getSignUp(
                    name = name.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    email = email.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    password = password.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    gender = gender.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    designation = designation.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    hobbies = hobbies.toRequestBody("text/plain".toMediaTypeOrNull()), // Convert to RequestBody
                    profile_image = profilePart
                )

                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    println(signupResponse?.status);
                    val navigateToLogin = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(navigateToLogin)
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@SignupActivity,
                            "Sign Up Failed: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("SignupActivity", "Sign Up Failed: ${response.errorBody()?.string()}")
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@SignupActivity,
                        "Sign Up Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("SignupActivity", "Sign Up Error: ${e.localizedMessage}", e)
                }
            }
        }
    }
}