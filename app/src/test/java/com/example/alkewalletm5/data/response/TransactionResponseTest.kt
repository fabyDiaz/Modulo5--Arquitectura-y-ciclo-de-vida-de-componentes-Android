package com.example.alkewalletm5.data.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class TransactionResponseTest{
    @Test
    fun testTransactionResponseProperties() {
        // Crear un objeto TransactionResponse de ejemplo
        val transactionResponse = TransactionResponse(
            id = 1,
            amount = 5000,
            concept = "Salary",
            date = "2023-06-20",
            type = "Credit",
            accountId = 101,
            userId = 202,
            toAccountId = 303
        )

        // Verificar que las propiedades se hayan establecido correctamente
        assertEquals(1, transactionResponse.id)
        assertEquals(5000, transactionResponse.amount)
        assertEquals("Salary", transactionResponse.concept)
        assertEquals("2023-06-20", transactionResponse.date)
        assertEquals("Credit", transactionResponse.type)
        assertEquals(101, transactionResponse.accountId)
        assertEquals(202, transactionResponse.userId)
        assertEquals(303, transactionResponse.toAccountId)
    }

    @Test
    fun testTransactionResponseSerialization() {
        // Crear un objeto TransactionResponse de ejemplo
        val transactionResponse = TransactionResponse(
            id = 1,
            amount = 5000,
            concept = "Salary",
            date = "2023-06-20",
            type = "Credit",
            accountId = 101,
            userId = 202,
            toAccountId = 303
        )

        // Serializar el objeto a JSON usando Gson
        val gson = Gson()
        val json = gson.toJson(transactionResponse)

        // Verificar que la serialización fue exitosa
        assertEquals(
            "{\"id\":1,\"amount\":5000,\"concept\":\"Salary\",\"date\":\"2023-06-20\",\"type\":\"Credit\"," +
                    "\"accountId\":101,\"userId\":202,\"to_account_id\":303}",
            json
        )
    }

    @Test
    fun testTransactionResponseDeserialization() {
        // JSON de ejemplo
        val json = """
            {
                "id": 1,
                "amount": 5000,
                "concept": "Salary",
                "date": "2023-06-20",
                "type": "Credit",
                "accountId": 101,
                "userId": 202,
                "to_account_id": 303
            }
        """.trimIndent()

        // Deserializar el JSON a un objeto TransactionResponse usando Gson
        val gson = Gson()
        val transactionResponse = gson.fromJson(json, TransactionResponse::class.java)

        // Verificar que las propiedades se hayan deserializado correctamente
        assertEquals(1, transactionResponse.id)
        assertEquals(5000, transactionResponse.amount)
        assertEquals("Salary", transactionResponse.concept)
        assertEquals("2023-06-20", transactionResponse.date)
        assertEquals("Credit", transactionResponse.type)
        assertEquals(101, transactionResponse.accountId)
        assertEquals(202, transactionResponse.userId)
        assertEquals(303, transactionResponse.toAccountId)
    }
}