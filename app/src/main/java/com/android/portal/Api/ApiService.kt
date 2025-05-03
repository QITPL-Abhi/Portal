package com.android.portal

import com.android.portal.RequestBody.NotificationRequest
import com.android.portal.ResponseBody.DashboardResponse
import com.android.portal.ResponseBody.ViewProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("getLogin.php?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getlogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @Multipart
    @POST("getSignUp.php?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getSignUp(
        @Part("name") name: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody,
        @Part("password") password: okhttp3.RequestBody,
        @Part("gender") gender: okhttp3.RequestBody,
        @Part("designation") designation: okhttp3.RequestBody,
        @Part("hobbies") hobbies: okhttp3.RequestBody,
        @Part profile_image: MultipartBody.Part? // Removed the part name from the annotation
    ): Response<SignupResponse>

    @FormUrlEncoded
    @POST("getDataFromId.php?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getDashboardData(
        @Field("id") id: String
    ): Response<DashboardResponse>

    @FormUrlEncoded
    @POST("getDataFromId.php?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getViewData(
        @Field("id") id: String
    ): Response<ViewProfileResponse>

    @Multipart
    @POST("getUpdateLogin.php?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getUpdateLogin(
        @Part("loginId") loginId: okhttp3.RequestBody,
        @Part("name") name: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody,
        @Part("password") password: okhttp3.RequestBody,
        @Part("gender") gender: okhttp3.RequestBody,
        @Part("designation") designation: okhttp3.RequestBody,
        @Part("hobbies") hobbies: okhttp3.RequestBody,
    ): Response<SignupResponse>


    @POST("send-notification")
    suspend fun getNotifications(
        @Body request: NotificationRequest
    ): Response<Any>


}
