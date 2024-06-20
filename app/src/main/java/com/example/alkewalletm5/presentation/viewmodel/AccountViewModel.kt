package com.example.alkewalletm5.presentation.viewmodel

import android.accounts.Account
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.local.DestinatariosDataSet
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.AccountResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AccountViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel()  {

    private val _accounts = MutableLiveData<MutableList<AccountResponse>>()
    val accounts: LiveData<MutableList<AccountResponse>> get() = _accounts

    private val _error = MutableLiveData<String>()

    private val authManager = AuthManager(context)

    /*init {
        // Inicializa con una lista vacía
         fetchUserAccounts()
    }*/

    fun fetchUserAccounts() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val response = useCase.myAccount(token)
                    if (response.isSuccessful) {
                        _accounts.value = response.body()
                    } else {
                        Log.e("AccountViewModel", "Error al obtener cuentas: ${response.message()}")
                    }
                } else {
                    Log.e("AccountViewModel", "Token de autenticación no disponible")
                }
            } catch (e: Exception) {
                Log.e("AccountViewModel", "Error al obtener cuentas: ${e.message}")
            }
        }
    }



    fun getAccountById(accountId: Long): LiveData<AccountResponse> {
        val result = MutableLiveData<AccountResponse>()
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val account = useCase.getAccountsById(token, accountId)
                    result.postValue(account)
                }
            } catch (e: Exception) {
                _error.postValue("Error al obtener la cuenta del destinatario")
                Log.e("TransactionViewModel", "Error al obtener la cuenta del destinatario", e)
            }
        }
        return result
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

   /* fun sumarSaldoUsuario(monto: Double): Boolean {
        Log.i("USUARIO", "El nuevo saldo es ${_accounts.value.toString()}")
        _accounts.value?.let { userAccounts ->
            val userAccount = userAccounts[0]
            val nuevoSaldo = userAccount.money.toDouble() + monto
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
    }*/

    fun sumarSaldoUsuario(monto: Double): Boolean {
        Log.i("USUARIO", "Saldo actual de las cuentas: ${_accounts.value.toString()}")
        _accounts.value?.let { userAccounts ->
            val userAccount = userAccounts[0]
            val saldoActual = userAccount.money.toDouble()
            val nuevoSaldo = saldoActual + monto
            Log.i("USUARIO", "El nuevo saldo es $nuevoSaldo")

            return if (nuevoSaldo >= 0 && nuevoSaldo <= 5000000) {  // Verifica que el nuevo saldo no supere los 5 millones
                userAccount.money = nuevoSaldo.toString()
                viewModelScope.launch {
                    val token = authManager.getToken()
                    if (token != null) {
                        val response = useCase.updateAccount(token, userAccount)
                        if (response.isSuccessful) {
                            _accounts.postValue(_accounts.value) // Actualiza LiveData para notificar los cambios
                            Log.d("USUARIO", "Saldo actualizado: ${_accounts.value}")
                        } else {
                            Log.e("USUARIO", "Error al actualizar el saldo: ${response.message()}")
                        }
                    }
                }
                true
            } else {
                if (nuevoSaldo > 5000000) {
                    Log.e("USUARIO", "Error: El nuevo saldo supera el límite permitido de 5 millones")
                }
                false
            }
        }
        return false
    }

    private fun createAccountForUser(account: AccountResponse) {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val response = useCase.createAccount(token, account)
                    Log.d("CUENTA", "Token obtenido correctamente: $token")
                    Log.d("CUENTA", "Datos de la cuenta a crear: $account")
                    if (response.isSuccessful) {
                        val createdAccount = response.body()
                        Log.d("CUENTA", "Cuenta creada: ${createdAccount?.id}")
                        Log.d("CUENTA", "Cuenta creada con éxito: ${createdAccount?.id}, datos de la cuenta: $createdAccount")
                    } else {
                        _error.value = "Error al crear cuenta: ${response.message()}"
                        Log.e("CUENTA", "Error al crear cuenta: ${response.message()}")
                    }
                } else {
                    Log.e("CUENTA", "Token de autenticación no disponible")
                    _error.value = "Error: Token de autenticación no disponible"
                }
            } catch (e: HttpException) {
                _error.value = "Error: ${e.code()} ${e.message()}"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

}