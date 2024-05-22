package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.UsuariosDataSet
import com.example.alkewalletm5.data.model.Usuario

class UsuarioViewModel: ViewModel() {

    private val _usuarios = MutableLiveData<MutableList<Usuario>>()
    val usuarios: LiveData<MutableList<Usuario>> get() = _usuarios

    private val _usuarioLogueado = MutableLiveData<Usuario>()
    val usuarioLogueado: LiveData<Usuario> get() = _usuarioLogueado

    init {
        // Inicializa con una lista vacía o con datos iniciales
        _usuarios.value = UsuariosDataSet().ListaUsuarios()
    }

    fun setUsuarioLogueado(usuario: Usuario) {
        _usuarioLogueado.value = usuario
    }

    fun addUsuario(usuario: Usuario) {
        // currentList garantizamos que no modificamos la lista directamente en LiveData sin notificar a los observadores.
        val currentList = _usuarios.value ?: mutableListOf()
        currentList.add(usuario)
        _usuarios.value = currentList
    }

    fun autenticarUsuario(email: String, password: String): Usuario? {
        return _usuarios.value?.find { it.email == email && it.password == password }
    }

    private fun actualizarListaUsuarios(usuarioActualizado: Usuario) {
        _usuarios.value = _usuarios.value?.map {
            if (it.email == usuarioActualizado.email) usuarioActualizado else it
        }?.toMutableList()
    }


    fun restarSaldoUsuario(monto: Double): Boolean {
        _usuarioLogueado.value?.let { usuario ->
            val nuevoSaldo = usuario.saldo - monto
            return if (nuevoSaldo >= 0) {
                val usuarioActualizado = usuario.copy(saldo = nuevoSaldo)
                _usuarioLogueado.value = usuarioActualizado

                // Actualizar la lista de usuarios
               actualizarListaUsuarios(usuarioActualizado)
                true
            } else {
                false
            }
        }
        return false
    }

    fun sumarSaldoUsuario(monto: Double){
        _usuarioLogueado.value?.let { usuario ->
            val nuevoSaldo = usuario.saldo + monto
            val usuarioActualizado = usuario.copy(saldo = nuevoSaldo)
            _usuarioLogueado.value = usuarioActualizado

            actualizarListaUsuarios(usuarioActualizado)
        }

    }
}