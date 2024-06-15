package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
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

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            _user.value = useCase.getUserById(userId)
        }
    }

    fun createUserAndGetId(user: UserResponse) {
        viewModelScope.launch {
            val response = useCase.createUserApp(user)
            if (response.isSuccessful) {
                val createdUser = response.body()
                createdUser?.id?.let {
                    _createdUserId.value = it
                    Log.e("USUARIO", _createdUserId.value.toString())
                    // Crear cuenta para el nuevo usuario
                    val newAccount = AccountResponse(
                        id = 0,
                        creationDate = "", // Establecer la fecha de creación según lo necesario
                        money = 1000.0,
                        isBlocked = false,
                        userId = it,
                        updatedAt = "", // Estos valores serán establecidos por el servidor
                        createdAt = ""
                    )

                    createAccountForUser(newAccount)
                }
            } else {
                // Manejar el error de creación de usuario
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
            } catch (e: Exception) {
                _token.value = null
                Log.e("USUARIO", _token.value.toString())
            }
        }
    }

    private fun createAccountForUser(account: AccountResponse) {
        viewModelScope.launch {
            val response = useCase.createAccount(account)
            if (response.isSuccessful) {
                val createdAccount = response.body()
                Log.d("CUENTA", "Cuenta creada: ${createdAccount?.id}")
            } else {
                // Manejar el error de creación de cuenta
            }
        }
    }

    fun fetchLoggedUser() {
        viewModelScope.launch {
            val token = authManager.getToken()
            if (token != null) {
                val usuario = useCase.getUserByToken(token)
                _usuarioLogueado.value = usuario
                Log.i("USUARIO", _usuarioLogueado.toString())
            }
        }
    }

}