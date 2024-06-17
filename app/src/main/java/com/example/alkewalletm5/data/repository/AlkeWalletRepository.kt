package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.UserListResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

interface AlkeWalletRepository {
    //suspend fun fetchUserById(idUser: Long): UserResponse

    suspend fun createUser(user: UserResponse): Response<UserResponse>

    suspend fun login(email: String, password: String): String

    suspend fun getUserById(idUser: Long): UserResponse

    suspend fun getAccountsById(idUser: Long): AccountResponse

    suspend fun getAllUsers(): MutableList<UserResponse>

    suspend fun createAccount(account: AccountResponse): Response<AccountResponse>

    suspend fun getUserByToken(token: String): UserResponse
    suspend fun myAccount(token: String): MutableList<AccountResponse>
    suspend fun createTransaction(token: String,transaction: TransactionResponse): Response<TransactionResponse>

    suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse>




}