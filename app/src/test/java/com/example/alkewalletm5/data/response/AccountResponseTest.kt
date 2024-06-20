package com.example.alkewalletm5.data.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class AccountResponseTest{
        @Test
        fun testAccountResponseProperties() {
            // Crear un objeto AccountResponse de ejemplo
            val accountResponse = AccountResponse(
                id = 1,
                creationDate = "2024-06-20",
                money = "1000.00",
                isBlocked = false,
                userId = 10
            )

            // Verificar que las propiedades se hayan establecido correctamente
            assertEquals(1, accountResponse.id)
            assertEquals("2024-06-20", accountResponse.creationDate)
            assertEquals("1000.00", accountResponse.money)
            assertEquals(false, accountResponse.isBlocked)
            assertEquals(10, accountResponse.userId)
        }

        @Test
        fun testAccountResponseSerialization() {
            // Crear un objeto AccountResponse de ejemplo
            val accountResponse = AccountResponse(
                id = 1,
                creationDate = "2024-06-20",
                money = "1000.00",
                isBlocked = false,
                userId = 10
            )

            // Serializar el objeto a JSON usando Gson (o la biblioteca que estés usando)
            val gson = Gson()
            val json = gson.toJson(accountResponse)

            // Verificar que la serialización fue exitosa
            assertEquals(
                "{\"id\":1,\"creationDate\":\"2024-06-20\",\"money\":\"1000.00\",\"isBlocked\":false,\"userId\":10}",
                json
            )
        }
}