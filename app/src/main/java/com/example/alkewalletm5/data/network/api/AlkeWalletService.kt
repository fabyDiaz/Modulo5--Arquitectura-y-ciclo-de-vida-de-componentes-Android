package com.example.alkewalletm5.data.network.api

import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.LoginRequest
import com.example.alkewalletm5.data.response.LoginResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AlkeWalletService {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") idUser : Long) : UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserResponse): Response<UserResponse>
    @Headers("Content-type:application/json")
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
    //@GET("auth/me")
    //suspend fun myProfile(): UserDataResponse
    @GET("accounts/me")
    suspend fun myAccount(): MutableList<AccountResponse>

    @GET("users")
    suspend fun getAllUsers(): MutableList<UserResponse>

    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: Long): AccountResponse






}