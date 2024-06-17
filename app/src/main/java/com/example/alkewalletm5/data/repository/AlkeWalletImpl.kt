package com.example.alkewalletm5.data.repository

import android.util.Log
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.LoginRequest
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.UserListResponse
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

    override suspend fun myAccount(token: String): MutableList<AccountResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.myAccount("Bearer $token")
        }
    }

    override suspend fun createTransaction(
        token: String,
        transaction: TransactionResponse
    ): Response<TransactionResponse> {
        return withContext(Dispatchers.IO){
            apiservice.createTransaction("Bearer $token",transaction)
        }
    }

    override suspend fun getUserById(idUser: Long): UserResponse {
        return withContext(Dispatchers.IO){
            val user = apiservice.getUserById(idUser)
            user
        }
    }

    override suspend fun getAccountsById(token: String, idUser: Long): AccountResponse {
        return withContext(Dispatchers.IO) {
            apiservice.getAccountById("Bearer $token", idUser)
        }
    }

    override suspend fun getAllUsers(): MutableList<UserResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.getAllUsers()
        }
    }

    override suspend fun createAccount(account: AccountResponse): Response<AccountResponse> {
        return apiservice.createAccount(account)
    }

    override suspend fun getUserByToken(token: String): UserResponse {
        return withContext(Dispatchers.IO) {
            apiservice.getUserByToken("Bearer $token")
        }
    }

    override suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse> {
        return withContext(Dispatchers.IO) {
            Log.d("AlkeWalletImpl", "Token enviado: $token, página: $page")
            val response = apiservice.getUsersByPage("Bearer $token", page)
            Log.d("AlkeWalletImpl", "Respuesta obtenida: ${response.code()} - ${response.message()}")
            response
        }
    }

    override suspend fun updateAccount(
        token: String,
        account: AccountResponse
    ): Response<AccountResponse> {
        return withContext(Dispatchers.IO) {
            apiservice.updateAccount("Bearer $token", account.id, account)
        }
    }


}