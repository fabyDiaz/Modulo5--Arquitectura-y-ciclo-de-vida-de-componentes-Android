package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.AccountResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel() {


    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user
    private val _createdUserId = MutableLiveData<Long>()
    val createdUserId: LiveData<Long> get() = _createdUserId
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    private val _usuarioLogueado = MutableLiveData<UserResponse>()
    val usuarioLogueado: LiveData<UserResponse> get() = _usuarioLogueado

    private val authManager = AuthManager(context)

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun createUser(user:UserResponse){
        viewModelScope.launch {
            try {
                val response = useCase.createUserApp(user)
                if (response.isSuccessful) {
                    val createdUser = response.body()
                    createdUser?.id?.let {
                        _createdUserId.value = it
                        Log.d("USUARIO", "Usuario creado con ID: $it")
                    }
                } else {
                    _error.value = "Error al crear usuario: ${response.message()}"
                    Log.e("USUARIO", "Error al crear usuario: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Error al crear usuario: ${e.message}"
                Log.e("USUARIO", "Error al crear usuario: ${e.message}")
            }
        }
    }


   fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val token = useCase.login(email, password)
                authManager.saveToken(token)
                _token.value = token
                Log.e("USUARIO", _token.value.toString())
            } catch (e: HttpException) {
                _error.value = "Error: ${e.code()} ${e.message()}"
            } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
                _token.value = null
        }
        }
    }



    fun fetchLoggedUser() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val usuario = useCase.getUserByToken(token)
                    _usuarioLogueado.value = usuario
                    Log.i("USUARIO", _usuarioLogueado.value.toString())
                } else {
                    Log.e("USUARIO", "No se pudo obtener el token de autenticación")
                }
            } catch (e: Exception) {
                // Intenta cargar el usuario desde la base de datos local
                try {
                    val localUser = useCase.getLocalUser()
                    _usuarioLogueado.value = localUser
                } catch (dbException: Exception) {
                    Log.e("USUARIO", "Error al obtener el usuario: ${e.message}")
                    _error.value = "Error al obtener el usuario: ${e.message}"
                }
            }
        }
    }


}


