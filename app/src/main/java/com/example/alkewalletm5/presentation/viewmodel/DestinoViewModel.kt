package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch

class DestinoViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel()  {

    private val _usuarios = MutableLiveData<List<UserResponse>>()
    val usuarios: LiveData<List<UserResponse>> get() = _usuarios

    private val _account = MutableLiveData<AccountResponse>()
    val account: LiveData<AccountResponse> get() = _account

    private val _error = MutableLiveData<String>()

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

    fun obtenerCuentaPorUsuarioId(userId: Long) {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val account = useCase.getAccountsById(token, userId)
                    _account.postValue(account)
                } else {
                    _error.postValue("No se pudo obtener el token de autenticación")
                }
            } catch (e: Exception) {
                _error.postValue("Error al obtener la cuenta del usuario")
                Log.e("DestinatarioViewModel", "Error al obtener la cuenta del usuario", e)
            }
        }
    }
}