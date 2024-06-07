package com.example.alkewalletm5.domain

import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.UserResponse

class AlkeWalletUseCase(private val repository: AlkeWalletImpl) {

    suspend fun getUserByIdApp(userId: Long): UserResponse {
        return repository.fetchUserById(userId)
    }

}