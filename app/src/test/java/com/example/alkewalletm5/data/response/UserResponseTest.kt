package com.example.alkewalletm5.data.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class UserResponseTest{
    @Test
    fun testUserResponseProperties() {
        // Crear un objeto UserResponse de ejemplo
        val userResponse = UserResponse(
            id = 1,
            firstName = "Homero",
            lastName = "Simpson",
            email = "homero@example.com",
            password = "password123",
            points = 100,
            roleId = 2
        )

        // Verificar que las propiedades se hayan establecido correctamente
        assertEquals(1, userResponse.id)
        assertEquals("Homero", userResponse.firstName)
        assertEquals("Simpson", userResponse.lastName)
        assertEquals("homero@example.com", userResponse.email)
        assertEquals("password123", userResponse.password)
        assertEquals(100, userResponse.points)
        assertEquals(2, userResponse.roleId)
    }

    @Test
    fun testUserResponseSerialization() {
        // Crear un objeto UserResponse de ejemplo
        val userResponse = UserResponse(
            id = 1,
            firstName = "Homero",
            lastName = "Simpson",
            email = "homero@example.com",
            password = "password123",
            points = 100,
            roleId = 2
        )

        // Serializar el objeto a JSON usando Gson (o la biblioteca que estés usando)
        val gson = Gson()
        val json = gson.toJson(userResponse)

        // Verificar que la serialización fue exitosa
        assertEquals(
            "{\"id\":1,\"first_name\":\"Homero\",\"last_name\":\"Simpson\",\"email\":\"homero@example.com\"," +
                    "\"password\":\"password123\",\"points\":100,\"roleId\":2}",
            json
        )
    }
}