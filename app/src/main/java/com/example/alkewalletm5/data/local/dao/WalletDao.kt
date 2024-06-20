package com.example.alkewalletm5.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.UserLogged
import com.example.alkewalletm5.data.response.UserResponse
@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserResponse>)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserResponse>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<Long>): List<UserResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountResponse>)

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountResponse>

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccountByUserId(userId: Long): AccountResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionResponse>)

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    suspend fun getTransactionsByUserId(userId: Long): List<TransactionResponse>

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionResponse>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserResponse?

}