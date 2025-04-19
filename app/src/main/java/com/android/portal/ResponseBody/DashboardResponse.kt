package com.android.portal.ResponseBody

data class DashboardResponse(
    val `data`: Data,
    val message: String,
    val status: String
)

data class Data(
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