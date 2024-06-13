package com.example.alkewalletm5.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch

class UserViewModel(private val useCase: AlkeWalletUseCase) : ViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: MutableLiveData<UserResponse> get() = _user
    private val _createdUserId = MutableLiveData<Long>()
    val createdUserId: MutableLiveData<Long> get() = _createdUserId


    fun getUserById(userId: Long) {
        viewModelScope.launch {
            _user.value = useCase.getUserByIdApp(userId)
        }
    }

    fun createUserAndGetId(user: UserResponse) {
        viewModelScope.launch {
            val response = useCase.createUserApp(user)
            if (response.isSuccessful) {
                val createdUser = response.body()
                // Extraer el ID del usuario creado de la respuesta del servidor
                createdUser?.id?.let {
                    _createdUserId.value = it
                    Log.i("UserFragment", _createdUserId.value.toString())
                }
            } else {
                // Manejar el error de creación de usuario
            }
        }
    }
}