package com.example.alkewalletm5.data.response

data class UserListResponse (
    val data: List<UserResponse>, // La lista de usuarios de la página actual
    val totalPages: Int, // Total de páginas disponibles
    val currentPage: Int // Página actual
){

}