package com.example.alkewalletm5.domain

import android.util.Log
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.TransactionsListResponse
import com.example.alkewalletm5.data.response.UserListResponse
import com.example.alkewalletm5.data.response.UserLogged
import com.example.alkewalletm5.data.response.UserResponse
import retrofit2.Response

class AlkeWalletUseCase(private val repository: AlkeWalletImpl) {

    suspend fun createUserApp(user: UserResponse): Response<UserResponse> {
        return repository.createUser(user)
    }

    suspend fun login(email: String, password: String): String {
        return repository.login(email, password)
    }

    suspend fun myAccount(token: String): Response<MutableList<AccountResponse>> {
        return repository.myAccount(token)
    }
    suspend fun getUserById(idUser: Long): UserResponse {
        return repository.getUserById(idUser)
    }

    suspend fun getAccountsById(token: String, idAccount: Long): AccountResponse {
        return repository.getAccountsById(token, idAccount)
    }

    suspend fun getAllUsers(): MutableList<UserResponse>{
        return  repository.getAllUsers()

    }

    suspend fun createAccount(token: String, account: AccountResponse): Response<AccountResponse> {
        return repository.createAccount(token, account)
    }

    suspend fun getUserByToken(token: String): UserResponse {
        return repository.getUserByToken(token)
    }

    suspend fun getAllTransactionUser(token: String): TransactionsListResponse {
        return repository.getAllTransactionUser(token)
    }

    suspend fun createTransaction(
        token: String,
        transaction: TransactionResponse
    ):  Response<TransactionResponse> {
        return repository.createTransaction(token, transaction)
    }

    suspend fun updateAccount(token: String, account: AccountResponse): Response<AccountResponse> {
        return repository.updateAccount(token, account)
    }

    suspend fun getLocalUser(): UserResponse {
        return repository.getLocalUser()
    }

    suspend fun getLocalAccounts(): List<AccountResponse> {
        return repository.getLocalAccounts()
    }

    suspend fun getLocalTransactions(): List<TransactionResponse> {
        return repository.getLocalTransactions()
    }

    suspend fun getLocalUsers(page: Int): List<UserResponse> {
        return repository.getLocalUsers(page)
    }


    suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse> {
        // Verifica si la página solicitada está fuera de los límites esperados.
        if (page < 0) throw IllegalArgumentException("Page number cannot be negative")

        // Asumiendo que tienes un límite en el número de páginas
        val maxPages = 10 // Ajusta esto según tu lógica
        if (page > maxPages) {
            val emptyUserListResponse = UserListResponse(data = emptyList(), totalPages = maxPages, currentPage = page)
            return Response.success(emptyUserListResponse)
        }

        // Realiza la llamada al repositorio para obtener los usuarios de la página solicitada
        val users = repository.getUsersByPage(token, page)

        // Lógica adicional para manejar los usuarios obtenidos (si es necesario)
        // ...

        return users
    }

}