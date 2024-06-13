package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response

interface AlkeWalletRepository {
    suspend fun fetchUserById(idUser: Long): UserResponse

    suspend fun createUser(user: UserResponse): Response<UserResponse>

}