package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch

class DestinoViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel()  {

    private val _usuarios = MutableLiveData<List<UserResponse>>()
    val usuarios: LiveData<List<UserResponse>> get() = _usuarios

    private val authManager = AuthManager(context)

    private val currentPage = 2 // Definir la página que deseas cargar

    fun cargarUsuarios() {
        viewModelScope.launch {
            val token = authManager.getToken()
            if (token != null) {
                try {
                    Log.d("DestinoViewModel", "Token obtenido: $token")
                    val response = useCase.getUsersByPage(token, currentPage)
                    if (response.isSuccessful) {
                        _usuarios.value = response.body()?.data ?: emptyList()
                        Log.d("DestinoViewModel", "Usuarios cargados: ${_usuarios.value}")
                    } else {
                        Log.e("DestinoViewModel", "Error al cargar usuarios: ${response.message()} - ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("DestinoViewModel", "Excepción al cargar usuarios: ${e.message}")
                }
            } else {
                Log.e("DestinoViewModel", "No se pudo obtener el token de autenticación")
            }
        }
    }
}