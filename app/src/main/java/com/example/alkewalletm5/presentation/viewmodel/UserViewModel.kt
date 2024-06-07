package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch

class UserViewModel(private val useCase: AlkeWalletUseCase) : ViewModel() {

    private val _users = MutableLiveData<UserResponse>()

    val users: MutableLiveData<UserResponse>
        get() = _users

    fun getUserById(userId : Long) {
        viewModelScope.launch {
            _users.value = useCase.getUserByIdApp(userId)
        }
    }
}