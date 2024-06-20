package com.example.alkewalletm5.presentation.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.TransactionViewModel

class TransactionViewModelFactory (private val useCase: AlkeWalletUseCase, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(useCase,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}