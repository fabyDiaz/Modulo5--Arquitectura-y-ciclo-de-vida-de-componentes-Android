package com.example.alkewalletm5.data.response

data class AccountResponse(
    val id: Long,
    val creationDate: String,
    val money: String,
    val isBlocked: Boolean,
    val userId: Long,
    val updatedAt: String,
    val createdAt: String,
) {
}