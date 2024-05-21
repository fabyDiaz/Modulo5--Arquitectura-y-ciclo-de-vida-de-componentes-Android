package com.example.alkewalletm5.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Usuario

class UsuarioViewModel: ViewModel() {


    private val _usuarios = MutableLiveData<MutableList<Usuario>>()
    val usuarios: LiveData<MutableList<Usuario>> get() = _usuarios

    private val _usuarioLogueado = MutableLiveData<Usuario>()
    val usuarioLogueado: LiveData<Usuario> get() = _usuarioLogueado

    init {
        // Inicializa con una lista vacía o con datos iniciales
        _usuarios.value = mutableListOf(
            Usuario("Amanda", "Nose", "amanda@gmail.com", "amanda123", R.mipmap.amanda, 25000.0)
        )
    }

    fun setUsuarioLogueado(usuario: Usuario) {
        _usuarioLogueado.value = usuario
    }

    fun addUsuario(usuario: Usuario) {
        val currentList = _usuarios.value ?: mutableListOf()
        currentList.add(usuario)
        _usuarios.value = currentList
    }

    fun autenticarUsuario(email: String, password: String): Usuario? {
        return _usuarios.value?.find { it.email == email && it.password == password }
    }

    fun restarSaldoUsuario(monto: Double): Boolean {
        _usuarioLogueado.value?.let { usuario ->
            val nuevoSaldo = usuario.saldo - monto
            return if (nuevoSaldo >= 0) {
                val usuarioActualizado = usuario.copy(saldo = nuevoSaldo)
                _usuarioLogueado.value = usuarioActualizado

               /* // Actualizar la lista de usuarios
                _usuarios.value = _usuarios.value?.map {
                    if (it.email == usuario.email) usuarioActualizado else it
                }?.toMutableList()*/
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

            _usuarios.value = _usuarios.value?.map {
                if (it.email == usuario.email) usuarioActualizado else it
            }?.toMutableList()
        }

    }
}