package com.example.alkewalletm5.presentation.viewmodel

import android.accounts.Account
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch

class AccountViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel()  {

    private val _accounts = MutableLiveData<MutableList<AccountResponse>>()
    val accounts: LiveData<MutableList<AccountResponse>> get() = _accounts

    private val authManager = AuthManager(context)


    fun fetchUserAccounts() {
        viewModelScope.launch {
            val token = authManager.getToken()
            if (token != null) {
                val result = useCase.myAccount(token)
                _accounts.value = result
                Log.d("USUARIO", _accounts.value.toString())
            }
        }
    }

}