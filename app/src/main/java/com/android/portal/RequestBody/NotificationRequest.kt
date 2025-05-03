package com.android.portal.RequestBody

data class NotificationRequest(
    val token: String,
    val title: String,
    val body: String
)