package com.android.portal

data class LoginRequestBody(
    val email: String,
    val password: String,
    val apiKey: String
)
