package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletm5.domain.AlkeWalletUseCase

class DestinoViewModelFactory(private val useCase: AlkeWalletUseCase, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DestinoViewModel::class.java)) {
            return DestinoViewModel(useCase,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}