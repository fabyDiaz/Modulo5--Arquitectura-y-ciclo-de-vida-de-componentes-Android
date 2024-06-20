package com.example.alkewalletm5.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
@Entity(tableName = "transactions")
data class TransactionResponse(
    @PrimaryKey
    val id: Long,
    val amount: Long,
    val concept: String,
    val date: String,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long
){
}