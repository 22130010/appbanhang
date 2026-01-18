package com.example.appbanhang.network
import com.example.appbanhang.data.LoginRequest

import com.example.appbanhang.data.RegisterRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<String>


    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<String>
}
