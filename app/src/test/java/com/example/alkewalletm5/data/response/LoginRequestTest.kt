package com.example.alkewalletm5.data.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class LoginRequestTest{
    @Test
    fun testLoginRequestProperties() {
        // Crear un objeto LoginRequest de ejemplo
        val loginRequest = LoginRequest(
            email = "test@example.com",
            password = "password123"
        )

        // Verificar que las propiedades se hayan establecido correctamente
        assertEquals("test@example.com", loginRequest.email)
        assertEquals("password123", loginRequest.password)
    }

    @Test
    fun testLoginRequestSerialization() {
        // Crear un objeto LoginRequest de ejemplo
        val loginRequest = LoginRequest(
            email = "test@example.com",
            password = "password123"
        )

        // Serializar el objeto a JSON usando Gson
        val gson = Gson()
        val json = gson.toJson(loginRequest)

        // Verificar que la serialización fue exitosa
        assertEquals(
            "{\"email\":\"test@example.com\",\"password\":\"password123\"}",
            json
        )
    }

    @Test
    fun testLoginRequestDeserialization() {
        // JSON de ejemplo
        val json = """
            {
                "email": "test@example.com",
                "password": "password123"
            }
        """.trimIndent()

        // Deserializar el JSON a un objeto LoginRequest usando Gson
        val gson = Gson()
        val loginRequest = gson.fromJson(json, LoginRequest::class.java)

        // Verificar que las propiedades se hayan deserializado correctamente
        assertEquals("test@example.com", loginRequest.email)
        assertEquals("password123", loginRequest.password)
    }
}