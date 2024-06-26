package com.example.alkewalletm5.data.response

import org.junit.Assert.*
import org.junit.Test

class TransactionsListResponseTest{
    @Test
    fun `test TransactionResponse creation`() {
        val transaction = TransactionResponse(
            id = 1L,
            amount = 1000L,
            concept = "Envío de dinero",
            date = "2024-06-24",
            type = "Send",
            accountId = 1L,
            userId = 1L,
            toAccountId = 2L
        )

        assertEquals(1L, transaction.id)
        assertEquals(1000L, transaction.amount)
        assertEquals("Envío de dinero", transaction.concept)
        assertEquals("2024-06-24", transaction.date)
        assertEquals("Send", transaction.type)
        assertEquals(1L, transaction.accountId)
        assertEquals(1L, transaction.userId)
        assertEquals(2L, transaction.toAccountId)
    }

    @Test
    fun `test default values`() {
        val transaction = TransactionResponse(
            id = 0L,
            amount = 0L,
            concept = "",
            date = "",
            type = "",
            accountId = 0L,
            userId = 0L,
            toAccountId = 0L
        )

        assertEquals(0L, transaction.id)
        assertEquals(0L, transaction.amount)
        assertEquals("", transaction.concept)
        assertEquals("", transaction.date)
        assertEquals("", transaction.type)
        assertEquals(0L, transaction.accountId)
        assertEquals(0L, transaction.userId)
        assertEquals(0L, transaction.toAccountId)
    }
}