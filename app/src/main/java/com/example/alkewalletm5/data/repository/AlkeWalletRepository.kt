package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response

interface AlkeWalletRepository {
    //suspend fun fetchUserById(idUser: Long): UserResponse

    suspend fun createUser(user: UserResponse): Response<UserResponse>

    suspend fun login(email: String, password: String): String

    suspend fun myAccount(): MutableList<AccountResponse>
    suspend fun getUserById(idUser: Long): UserResponse

    suspend fun getAccountsById(idUser: Long): AccountResponse

    suspend fun getAllUsers(): MutableList<UserResponse>


}