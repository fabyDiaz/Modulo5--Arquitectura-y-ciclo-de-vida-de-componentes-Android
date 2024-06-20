package com.example.alkewalletm5.data.response

import androidx.room.Entity

@Entity(tableName = "login")
data class LoginRequest(
    val email: String, val password: String
) {
}