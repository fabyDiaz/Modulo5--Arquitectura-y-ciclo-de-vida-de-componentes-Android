package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletm5.domain.AlkeWalletUseCase

class UserViewModelFactory(private val useCase: AlkeWalletUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}