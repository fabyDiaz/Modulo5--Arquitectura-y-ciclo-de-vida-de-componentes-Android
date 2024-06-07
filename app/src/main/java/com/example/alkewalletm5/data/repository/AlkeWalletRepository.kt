package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.response.UserResponse

interface AlkeWalletRepository {
    suspend fun fetchUserById(idUserService: Long): UserResponse
}