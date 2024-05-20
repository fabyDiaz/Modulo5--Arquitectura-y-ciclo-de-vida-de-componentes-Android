package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Usuario

class UsuarioViewModel: ViewModel() {


    private val _usuarios = MutableLiveData<MutableList<Usuario>>()
    val usuarios: LiveData<MutableList<Usuario>> get() = _usuarios

    init {
        // Inicializa con una lista vacía o con datos iniciales
        _usuarios.value = mutableListOf(
            Usuario("Amanda", "Nose", "amanda@gmail.com", "amanda123", R.mipmap.amanda, 25000.0)
        )
    }

    fun addUsuario(usuario: Usuario) {
        val currentList = _usuarios.value ?: mutableListOf()
        currentList.add(usuario)
        _usuarios.value = currentList
    }
}