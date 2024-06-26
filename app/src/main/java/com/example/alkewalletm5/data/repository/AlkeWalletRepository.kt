package com.example.alkewalletm5.data.repository

import com.example.alkewalletm5.data.model.Usuario
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.TransactionsListResponse
import com.example.alkewalletm5.data.response.UserListResponse
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response

interface AlkeWalletRepository {
    //suspend fun fetchUserById(idUser: Long): UserResponse

    suspend fun createUser(user: UserResponse): Response<UserResponse>

    suspend fun login(email: String, password: String): String

    suspend fun getUserById(idUser: Long): UserResponse

    suspend fun getAccountsById(token: String, idAccount: Long): AccountResponse

    suspend fun getAllUsers(): MutableList<UserResponse>

    suspend fun createAccount(token: String, account: AccountResponse): Response<AccountResponse>

    suspend fun getUserByToken(token: String): UserResponse
    suspend fun myAccount(token: String): Response<MutableList<AccountResponse>>
    suspend fun createTransaction(token: String,transaction: TransactionResponse): Response<TransactionResponse>

    suspend fun getAllTransactionUser(token: String): TransactionsListResponse

    suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse>

    suspend fun updateAccount(token: String,account: AccountResponse): Response<AccountResponse>

    suspend fun getLocalUser(userId: Long): UserResponse
    suspend fun getLocalAccounts(): List<AccountResponse>
    suspend fun getLocalTransactions(): List<TransactionResponse>

    suspend fun getLocalUsers(page: Int): List<UserResponse>

    suspend fun insertLocalUser(localUser: Usuario)

    suspend fun updateLocalUser(localUser: Usuario)

    suspend fun getLocalUserById(userId: Long): Usuario?

    suspend fun insertUser(user: UserResponse)




}