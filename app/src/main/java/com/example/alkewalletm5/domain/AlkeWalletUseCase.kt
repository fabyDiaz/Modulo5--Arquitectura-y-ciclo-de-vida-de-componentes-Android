package com.example.alkewalletm5.domain

import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response

class AlkeWalletUseCase(private val repository: AlkeWalletImpl) {


    suspend fun createUserApp(user: UserResponse): Response<UserResponse> {
        return repository.createUser(user)
    }

    suspend fun login(email: String, password: String): String {
        return repository.login(email, password)
    }

    suspend fun myAccount(): MutableList<AccountResponse> {
        return repository.myAccount()
    }
    suspend fun getUserById(idUser: Long): UserResponse {
        return repository.getUserById(idUser)
    }

    suspend fun getAccountsById(idUser: Long): AccountResponse {
        return repository.getAccountsById(idUser)
    }

    suspend fun getAllUsers(): MutableList<UserResponse>{
        return  repository.getAllUsers()

    }

    suspend fun createAccount(account: AccountResponse): Response<AccountResponse> {
        return repository.createAccount(account)
    }

    suspend fun getUserByToken(token: String): UserResponse {
        return repository.getUserByToken(token)
    }


}