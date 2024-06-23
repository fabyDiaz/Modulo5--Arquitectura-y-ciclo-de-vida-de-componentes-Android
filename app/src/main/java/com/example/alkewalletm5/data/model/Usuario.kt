package com.example.alkewalletm5.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alkewalletm5.R
/**
 *  Clase Destinatario
 *  @author Fabiola Díaz
 *  @since v1.1 24/05/2024
 *
 * Clase de datos que representa un Usuario (quien utiliza la app)

 */
@Entity(tableName = "local_users")
data class Usuario(
    @PrimaryKey
    val id: Long,
    val nombre: String,
    val apellido: String,
    val email: String,
    val imgPerfil: String? = null,
) {

}