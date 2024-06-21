package com.example.alkewalletm5.presentation.viewmodel

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
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AccountViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel()  {

    private val _accounts = MutableLiveData<List<AccountResponse>>()
    val accounts: LiveData<List<AccountResponse>> get() = _accounts

    private val _account = MutableLiveData<AccountResponse?>()
    val account: MutableLiveData<AccountResponse?> get() = _account

    private val _error = MutableLiveData<String>()

    private val authManager = AuthManager(context)

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
                // Intenta cargar las cuentas desde la base de datos local
                try {
                    val localAccounts = useCase.getLocalAccounts()
                    _accounts.value = localAccounts
                } catch (dbException: Exception) {
                    Log.e("AccountViewModel", "Error al obtener cuentas: ${e.message}")
                }
            }
        }
    }

    fun obtenerUserAccounts(userId: Long) {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val response = useCase.myAccount(token)
                    if (response.isSuccessful) {
                        _accounts.value = response.body()?.filter { it.userId == userId }
                    } else {
                        Log.e("AccountViewModel", "Error al obtener cuentas: ${response.message()}")
                    }
                } else {
                    Log.e("AccountViewModel", "Token de autenticación no disponible")
                }
            } catch (e: Exception) {
                // Intenta cargar las cuentas desde la base de datos local
                try {
                    val localAccounts = useCase.getLocalAccounts()
                    _accounts.value = localAccounts.filter { it.userId == userId }
                } catch (dbException: Exception) {
                    Log.e("AccountViewModel", "Error al obtener cuentas: ${e.message}")
                }
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

    fun createAccountForUser(idUser:Long) {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    val formattedDate = dateFormat.format(currentDate)
                    val account = AccountResponse(
                        id = 0, // El ID será generado por la base de datos
                        creationDate = formattedDate,
                        money = "1000", // Saldo inicial
                        isBlocked = false,
                        userId = idUser // ID del usuario logueado
                    )
                    val response = useCase.createAccount(token, account)
                    if (response.isSuccessful) {
                        val createdAccount = response.body()
                        _account.value = createdAccount

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