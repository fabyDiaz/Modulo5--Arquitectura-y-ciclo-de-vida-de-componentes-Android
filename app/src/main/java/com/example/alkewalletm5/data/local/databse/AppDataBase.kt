package com.example.alkewalletm5.data.local.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alkewalletm5.data.local.dao.WalletDao
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.UserResponse

@Database(entities = [UserResponse::class, AccountResponse::class, TransactionResponse::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase(){

    abstract fun  WalletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "wallet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}