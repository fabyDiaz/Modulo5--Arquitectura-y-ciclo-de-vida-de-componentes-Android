package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.data.local.DestinatariosDataSet
import com.example.alkewalletm5.data.model.Destinatario

class DestinatarioViewModel: ViewModel() {

    private val _destinatarios = MutableLiveData<MutableList<Destinatario>>()
    val destinatarios: LiveData<MutableList<Destinatario>> get() = _destinatarios

    init {
        // Inicializa con una lista vacía
        _destinatarios.value = DestinatariosDataSet().ListaDestintarios()
    }

    fun setLisDestinatariosData(listaDestinatarios: MutableList<Destinatario>){
        _destinatarios.value = listaDestinatarios
    }

    fun getLiveDataObserver(): LiveData<MutableList<Destinatario>>{
        return _destinatarios
    }


}