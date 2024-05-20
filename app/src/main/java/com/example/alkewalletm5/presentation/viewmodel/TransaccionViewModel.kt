package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.domain.TransaccionUseCase

class TransaccionViewModel: ViewModel() {

    private val _transacciones = MutableLiveData<MutableList<Transaccion>>()
    val transaccionUseCase  =TransaccionUseCase()

    init {
        // Inicializa con una lista vacía o con datos iniciales
        _transacciones.value = mutableListOf()
    }

    fun setListTransactionsData(listTransaction: MutableList<Transaccion>){
        _transacciones.value = listTransaction
    }
    //apa que comunica con el observador, la actividad
    fun getTransactionsList(userId: String){
        setListTransactionsData(transaccionUseCase.getAllTransactionForUser(userId))
    }

    fun getLiveDataObserver(): LiveData<MutableList<Transaccion>>{
        return _transacciones
    }

    fun addTransaccion(transaccion: Transaccion) {
        _transacciones.value?.add(transaccion)
        _transacciones.value = _transacciones.value // Trigger LiveData observer
    }

   /* private val _transacciones = MutableLiveData<MutableList<Transaccion>>()
    val transacciones: LiveData<MutableList<Transaccion>> get() = _transacciones

    init {
        // Inicializa con una lista vacía o con datos iniciales
        _transacciones.value = mutableListOf()
    }

    fun addTransaccion(transaccion: Transaccion) {
        _transacciones.value?.add(transaccion)
        _transacciones.value = _transacciones.value // Trigger LiveData observer
    }*/

}