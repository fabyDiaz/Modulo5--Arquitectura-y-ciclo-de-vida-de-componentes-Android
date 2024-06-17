package com.example.alkewalletm5.data.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TransactionResponse(
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