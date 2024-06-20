package com.example.alkewalletm5.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountResponse(
    @PrimaryKey
    val id: Long,
    val creationDate: String,
    var money: String,
    val isBlocked: Boolean,
    val userId: Long
) {
}