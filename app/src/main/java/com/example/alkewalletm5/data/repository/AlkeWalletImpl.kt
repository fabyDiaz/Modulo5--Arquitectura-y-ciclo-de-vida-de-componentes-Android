package com.example.alkewalletm5.data.repository

import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.alkewalletm5.data.local.dao.WalletDao
import com.example.alkewalletm5.data.model.Usuario
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.LoginRequest
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.TransactionsListResponse
import com.example.alkewalletm5.data.response.UserListResponse
import com.example.alkewalletm5.data.response.UserLogged
import com.example.alkewalletm5.data.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class AlkeWalletImpl(private var apiservice: AlkeWalletService, private val walletDao: WalletDao): AlkeWalletRepository{

    override suspend fun createUser(user: UserResponse): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiservice.createUser(user)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { walletDao.insertUsers(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }
   override suspend fun login(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val response = apiservice.login(LoginRequest(email, password))
            Log.i("TOKEN", response.accessToken)
            response.accessToken
        }
    }

    override suspend fun myAccount(token: String): Response<MutableList<AccountResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = apiservice.myAccount("Bearer $token")
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { walletDao.insertAccounts(it) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.success(walletDao.getAllAccounts().toMutableList())
            }
        }
    }

    override suspend fun createTransaction(
        token: String,
        transaction: TransactionResponse
    ): Response<TransactionResponse> {
        return withContext(Dispatchers.IO){
            val response = apiservice.createTransaction("Bearer $token", transaction)
            if (response.isSuccessful) {
                response.body()?.let { walletDao.insertTransactions(listOf(it)) }
            }
            response
        }
    }

    override suspend fun getAllTransactionUser(token: String): TransactionsListResponse {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = apiservice.getAllTransactionUser("Bearer $token")
                walletDao.insertTransactions(apiResponse.data)
                apiResponse
            } catch (e: Exception) {
                TransactionsListResponse(null, null, walletDao.getAllTransactions())
            }
        }
    }


    override suspend fun getUserById(idUser: Long): UserResponse {
        return withContext(Dispatchers.IO){
            val user = walletDao.getUsersByIds(listOf(idUser)).firstOrNull()
            user ?: apiservice.getUserById(idUser).also { walletDao.insertUsers(listOf(it)) }
        }
    }

    override suspend fun getAccountsById(token: String, idAccount: Long): AccountResponse {
        return withContext(Dispatchers.IO) {

            val account = walletDao.getAccountByUserId(idAccount)
            account ?: apiservice.getAccountById("Bearer $token", idAccount).also { walletDao.insertAccounts(listOf(it)) }
           // apiservice.getAccountById("Bearer $token", idAccount)
        }
    }

    override suspend fun getAllUsers(): MutableList<UserResponse> {
        return withContext(Dispatchers.IO) {
            val users = try {
                val apiUsers = apiservice.getAllUsers()
                walletDao.insertUsers(apiUsers)
                apiUsers
            } catch (e: Exception) {
                walletDao.getAllUsers().toMutableList()
            }
            users
        }
    }

    override suspend fun createAccount(token: String, account: AccountResponse): Response<AccountResponse> {
       // return apiservice.createAccount("Bearer $token", account)
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiservice.createAccount("Bearer $token", account)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { walletDao.insertAccounts(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }

    override suspend fun getUserByToken(token: String): UserResponse {
        return withContext(Dispatchers.IO) {
            apiservice.getUserByToken("Bearer $token")
        }
    }

    override suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiservice.getUsersByPage("Bearer $token", page)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.data?.let { walletDao.insertUsers(it) }
                }
                apiResponse
            } catch (e: Exception) {
                val users = walletDao.getAllUsers()
                Response.success(UserListResponse(users, users.size / 20, page))
            }
            response
        }
    }

    override suspend fun updateAccount(
        token: String,
        account: AccountResponse
    ): Response<AccountResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiservice.updateAccount("Bearer $token", account.id, account)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { walletDao.insertAccounts(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }

    override suspend fun getLocalUser(userId: Long): UserResponse {
        return withContext(Dispatchers.IO) {
            walletDao.getUser(userId) ?: throw NoSuchElementException("User not found locally")
        }
    }

    override suspend fun getLocalAccounts(): List<AccountResponse> {
        return withContext(Dispatchers.IO) {
            walletDao.getAllAccounts()
        }
    }

    override suspend fun getLocalTransactions(): List<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            walletDao.getAllTransactions()
        }
    }

    override suspend fun getLocalUsers(page: Int): List<UserResponse> {
        return withContext(Dispatchers.IO) {
            walletDao.getAllUsers()
        }
    }

    override suspend fun insertLocalUser(localUser: Usuario) {
        walletDao.insertLocalUser(localUser)
    }

    override suspend fun updateLocalUser(localUser: Usuario) {
        walletDao.updateLocalUser(localUser)
    }

    override suspend fun getLocalUserById(userId: Long): Usuario? {
        return walletDao.getLocalUserById(userId)
    }

    override suspend fun insertUser(user: UserResponse) {
        walletDao.insertUsers(listOf(user))
    }


}