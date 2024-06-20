package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.TransactionsListResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel() {

    private val _transactions = MutableLiveData<TransactionsListResponse>()
    val transactions: LiveData<TransactionsListResponse> get() = _transactions

    private val _transaction = MutableLiveData<TransactionResponse?>()
    val transaction: MutableLiveData<TransactionResponse?> get() = _transaction

    private val authManager = AuthManager(context)

    fun createTransaction(amount: Long, concept: String, type: String,  accountId: Long, userId: Long) {
        viewModelScope.launch {
            // Obtener el token de autenticación
            val token = authManager.getToken()
            if (token != null) {
                // Construir el objeto de transacción
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)

                val transaction = TransactionResponse(
                    id = 0,
                    amount = amount,
                    concept = concept,
                    date = formattedDate,  // Fecha actual
                    type = type,  // Tipo de transacción (suponiendo que es un pago)
                    accountId = accountId,  // ID de la cuenta del usuario que realiza la transacción
                    userId = userId,  // ID del usuario que realiza la transacción
                    toAccountId = 5  // ID de la cuenta destino
                )

                try {
                    // Llamar al caso de uso para crear la transacción
                    val response = useCase.createTransaction(token, transaction)

                    if (response.isSuccessful) {
                        val createdTransaction = response.body()
                        _transaction.value = createdTransaction

                        Log.d("USUARIO", "Transacción creada: ${createdTransaction?.id}")

                        // Agregar la nueva transacción a la lista existente
                        createdTransaction?.let {
                            val updatedTransactions = _transactions.value?.data?.toMutableList() ?: mutableListOf()
                            updatedTransactions.add(it)

                            _transactions.value = TransactionsListResponse(
                                previousPage = _transactions.value?.previousPage,
                                nextPage = _transactions.value?.nextPage,
                                data = updatedTransactions
                            )

                        }
                    } else {
                        Log.e("USUARIO", "Error al crear la transacción: ${response.message()}")
                        // Manejar el error según sea necesario
                    }
                } catch (e: Exception) {
                    Log.e("USUARIO", "Excepción al crear la transacción: ${e.message}")
                    // Manejar la excepción según sea necesario
                }
            } else {
                // Manejar la falta de token según sea necesario
                Log.e("USUARIO", "No se pudo obtener el token de autenticación")
            }
        }
    }


    fun getAlltransactions() {
        viewModelScope.launch {
            val token = authManager.getToken()
            if (token != null) {
                try {
                    val transactions = useCase.getAllTransactionUser(token)
                    _transactions.value = transactions
                    Log.i("USUARIO", "Las transacciones son: "+_transactions.value.toString())
                    // Imprimir las transacciones una por una
                    transactions.data.forEach { transaction ->
                        Log.i("USUARIO", "Transacción - ID: ${transaction.id}, Amount: ${transaction.amount}, Concept: ${transaction.concept}")
                    }
                } catch (e: Exception) {
                    Log.e("USUARIO", "Error al obtener las transacciones: ${e.message}")
                }
            } else {
                Log.e("USUARIO", "No se pudo obtener el token de autenticación")
            }
        }
    }

}