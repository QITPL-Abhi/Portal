package com.android.portal.ResponseBody

data class ViewProfileResponse(
    val data: ViewProfileData,
    val message: String,
    val status: String
)

data class ViewProfileData(
    val created_at: String,
    val designation: String,
    val email: String,
    val gender: String,
    val hobbies: String,
    val id: String,
    val name: String,
    val password: String,
    val profile_image: String
)