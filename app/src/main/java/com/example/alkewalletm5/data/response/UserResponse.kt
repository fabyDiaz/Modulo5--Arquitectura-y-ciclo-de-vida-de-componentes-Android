package com.example.alkewalletm5.data.response

import com.google.gson.annotations.SerializedName

class UserResponse (
    val id: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val points: Long,
    val roleId: Long,
    val createdAt: String,
    val updatedAt: String,
) {

}