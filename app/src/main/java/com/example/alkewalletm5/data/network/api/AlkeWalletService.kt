package com.example.alkewalletm5.data.network.api

import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlkeWalletService {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") idUser : Long) : UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserResponse): Response<UserResponse>



}