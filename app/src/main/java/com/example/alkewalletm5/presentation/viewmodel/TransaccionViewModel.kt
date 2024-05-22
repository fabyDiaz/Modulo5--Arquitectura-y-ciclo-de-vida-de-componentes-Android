package com.example.alkewalletm5.presentation.viewmodel

import android.content.BroadcastReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.data.model.Transaccion

class TransaccionViewModel: ViewModel() {

    private val _transacciones = MutableLiveData<MutableList<Transaccion>>()
    val transacciones: LiveData<MutableList<Transaccion>> get() = _transacciones

    private val _transaccion = MutableLiveData<Transaccion>()
    val transaccion: LiveData<Transaccion> get() = _transaccion

    fun nuevaTransaccion(fotoPerfil: Int, idReceiver: String, monto: Double, icono: Int, fecha: String) {
        val nuevaTransaccion = Transaccion(
            fotoPerfil = fotoPerfil,
            idReceriver = idReceiver,
            monto = monto,
            icono = icono,
            fecha = fecha
        )
        // Asignamos la nueva transacción al LiveData _transaccion
        _transaccion.value = nuevaTransaccion
    }


    init {
        // Inicializa con una lista vacía
        _transacciones.value = mutableListOf()
    }

    fun setListTransactionsData(listaTransacciones: MutableList<Transaccion>){
        _transacciones.value = listaTransacciones
    }

    fun getLiveDataObserver(): LiveData<MutableList<Transaccion>>{
        return _transacciones
    }

   /* fun addTransaccion(transaccion: Transaccion) {
        // currentList garantizamos que no modificamos la lista directamente en LiveData sin notificar a los observadores.
        val currentList = _transacciones.value ?: mutableListOf()
        currentList.add(transaccion)
        _transacciones.value = currentList
    }*/

    fun addTransaccion() {
        // Obtiene el valor actual de _transaccion
        val nuevaTransaccion = _transaccion.value
        // Verifica si la transacción no es nula antes de agregarla a la lista
        nuevaTransaccion?.let {
            // Obtiene la lista actual de transacciones
            val currentList = _transacciones.value ?: mutableListOf()
            // Agrega la nueva transacción a la lista
            currentList.add(it)
            // Actualiza el valor de _transacciones con la nueva lista que contiene la transacción agregada
            _transacciones.value = currentList
        }
    }

}