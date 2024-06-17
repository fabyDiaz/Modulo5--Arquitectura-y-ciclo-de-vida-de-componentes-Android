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

    fun restarSaldoUsuario(monto: Double): Boolean {
        Log.i("USUARIO", "El nuevo saldo es ${_accounts.value.toString()}")
        _accounts.value?.let { userAccounts ->
            val userAccount = userAccounts[0]
            val nuevoSaldo = userAccount.money.toDouble() - monto
            Log.i("USUARIO", "El nuevo saldo es $nuevoSaldo")
            Log.i("USUARIO", "El nuevo saldo es ${_accounts.value.toString()}")
            return if (nuevoSaldo >= 0) {
                userAccount.money = nuevoSaldo.toString()
                viewModelScope.launch {
                    val token = authManager.getToken()
                    if (token != null) {
                        val response = useCase.updateAccount(token, userAccount)
                        if (response.isSuccessful) {
                            _accounts.value = _accounts.value // Actualiza LiveData para notificar los cambios
                            Log.d("USUARIO", "Saldo actualizado: ${_accounts.value}")
                        } else {
                            Log.e("USUARIO", "Error al actualizar el saldo: ${response.message()}")
                        }
                    }
                }
                true
            } else {
                false
            }
        }
        return false
    }

}