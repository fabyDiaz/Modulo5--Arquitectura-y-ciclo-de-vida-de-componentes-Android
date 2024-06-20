package com.example.alkewalletm5.data.network.api

import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.LoginRequest
import com.example.alkewalletm5.data.response.LoginResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.TransactionsListResponse
import com.example.alkewalletm5.data.response.UserListResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AlkeWalletService {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") idUser : Long) : UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserResponse): Response<UserResponse>
    @Headers("Content-type:application/json")
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("users") //Falta agregar el token
    suspend fun getAllUsers(): MutableList<UserResponse>

    @GET("accounts/{id}") //Falta agregar el token
    suspend fun getAccountById(@Header("Authorization") token: String, @Path("id") id: Long): AccountResponse

    @POST("accounts")
    suspend fun createAccount(@Header("Authorization") token: String,@Body account: AccountResponse): Response<AccountResponse>
    @GET("auth/me")
    suspend fun getUserByToken(@Header("Authorization") token: String): UserResponse

    @GET("accounts/me")
    suspend fun myAccount(@Header("Authorization") token: String): Response<MutableList<AccountResponse>>
    @POST("transactions")
    suspend fun createTransaction(@Header("Authorization") token: String, @Body transaction: TransactionResponse): Response<TransactionResponse>

    @GET("transactions")
    suspend fun getAllTransactionUser(@Header("Authorization") token: String): TransactionsListResponse

    @GET("users")
    suspend fun getUsersByPage(@Header("Authorization") token: String, @Query("page") page: Int): Response<UserListResponse>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body account: AccountResponse
    ): Response<AccountResponse>


}