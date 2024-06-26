package com.example.alkewalletm5.data.response

import org.junit.Assert.*
import org.junit.Test

class UserListResponseTest{

    @Test
    fun `test UserListResponse creation`() {
        val users = listOf(
            UserResponse(
                id = 1L,
                firstName = "Alice",
                lastName = "Smith",
                email = "alice@example.com",
                password = "password123",
                points = 100L,
                roleId = 1L
            ),
            UserResponse(
                id = 2L,
                firstName = "Bob",
                lastName = "Johnson",
                email = "bob@example.com",
                password = "password456",
                points = 200L,
                roleId = 2L
            )
        )
        val userListResponse = UserListResponse(
            data = users,
            totalPages = 5,
            currentPage = 1
        )

        assertEquals(2, userListResponse.data.size)
        assertEquals(5, userListResponse.totalPages)
        assertEquals(1, userListResponse.currentPage)

        val user1 = userListResponse.data[0]
        assertEquals(1L, user1.id)
        assertEquals("Alice", user1.firstName)
        assertEquals("Smith", user1.lastName)
        assertEquals("alice@example.com", user1.email)
        assertEquals("password123", user1.password)
        assertEquals(100L, user1.points)
        assertEquals(1L, user1.roleId)

        val user2 = userListResponse.data[1]
        assertEquals(2L, user2.id)
        assertEquals("Bob", user2.firstName)
        assertEquals("Johnson", user2.lastName)
        assertEquals("bob@example.com", user2.email)
        assertEquals("password456", user2.password)
        assertEquals(200L, user2.points)
        assertEquals(2L, user2.roleId)
    }

    @Test
    fun `test empty UserListResponse`() {
        val userListResponse = UserListResponse(
            data = emptyList(),
            totalPages = 0,
            currentPage = 0
        )

        assertTrue(userListResponse.data.isEmpty())
        assertEquals(0, userListResponse.totalPages)
        assertEquals(0, userListResponse.currentPage)
    }
}