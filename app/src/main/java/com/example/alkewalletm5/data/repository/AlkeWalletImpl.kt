package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AlkeWalletImpl(private var apiservice: AlkeWalletService): AlkeWalletRepository{
    override suspend fun fetchUserById(idUser: Long): UserResponse {
        return withContext(Dispatchers.IO){
            val user = apiservice.getUserById(idUser)
            user
        }
    }

    override suspend fun createUser(user: UserResponse): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.createUser(user)
        }
    }
}