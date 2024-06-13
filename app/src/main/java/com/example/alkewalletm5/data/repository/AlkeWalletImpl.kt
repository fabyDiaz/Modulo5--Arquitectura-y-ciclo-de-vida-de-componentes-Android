package com.example.alkewalletm5.data.repository

import android.util.Log
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.LoginRequest
import com.example.alkewalletm5.data.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AlkeWalletImpl(private var apiservice: AlkeWalletService): AlkeWalletRepository{

    override suspend fun createUser(user: UserResponse): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.createUser(user)
        }
    }
   override suspend fun login(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val response = apiservice.login(LoginRequest(email, password))
            Log.i("TOKEN", response.accessToken)
            response.accessToken
        }
    }

    override suspend fun myAccount(): MutableList<AccountResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.myAccount()
        }
    }

    override suspend fun getUserById(idUser: Long): UserResponse {
        return withContext(Dispatchers.IO){
            val user = apiservice.getUserById(idUser)
            user
        }
    }

    override suspend fun getAccountsById(idUser: Long): AccountResponse {
        return withContext(Dispatchers.IO) {
            apiservice.getAccountById(idUser)
        }
    }

    override suspend fun getAllUsers(): MutableList<UserResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.getAllUsers()
        }
    }
}