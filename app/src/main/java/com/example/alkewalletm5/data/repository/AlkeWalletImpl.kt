package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlkeWalletImpl(private var apiservice: AlkeWalletService): AlkeWalletRepository{
    override suspend fun fetchUserById(userId: Long): UserResponse {
        return withContext(Dispatchers.IO){
            val user = apiservice.getUserById(userId)
            user
        }
    }


}