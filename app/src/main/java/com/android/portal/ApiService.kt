package com.android.portal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("getLogin?apiKey=ddd-otn745-jjgfd-uti-uiu")
    suspend fun getlogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>
}
