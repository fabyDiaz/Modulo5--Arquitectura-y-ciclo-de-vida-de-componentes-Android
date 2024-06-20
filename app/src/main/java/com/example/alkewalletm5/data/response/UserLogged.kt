package com.example.alkewalletm5.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "userlogged")
data class UserLogged (
    @PrimaryKey
    val id: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val points: Long,
    val roleId: Long
){
}