package com.example.alkewalletm5.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletm5.data.model.Usuario
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.data.network.api.AuthManager
import kotlinx.coroutines.launch
import retrofit2.HttpException


class UserViewModel(private val useCase: AlkeWalletUseCase, private val context: Context) : ViewModel() {


    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user
    private val _createdUserId = MutableLiveData<Long>()
    val createdUserId: LiveData<Long> get() = _createdUserId
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    private val _usuarioLogueado = MutableLiveData<UserResponse>()
    val usuarioLogueado: LiveData<UserResponse> get() = _usuarioLogueado

    private val _localUser = MutableLiveData<Usuario?>()
    val localUser: LiveData<Usuario?> get() = _localUser

    private val authManager = AuthManager(context)

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun createUser(user:UserResponse){
        viewModelScope.launch {
            try {
                val response = useCase.createUserApp(user)
                if (response.isSuccessful) {
                    val createdUser = response.body()
                    createdUser?.id?.let {
                        _createdUserId.value = it
                        Log.d("USUARIO", "Usuario creado con ID: $it")
                    }
                } else {
                    _error.value = "Error al crear usuario: ${response.message()}"
                    Log.e("USUARIO", "Error al crear usuario: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Error al crear usuario: ${e.message}"
                Log.e("USUARIO", "Error al crear usuario: ${e.message}")
            }
        }
    }


   fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val token = useCase.login(email, password)
                authManager.saveToken(token)
                _token.value = token
                Log.e("USUARIO", _token.value.toString())
            } catch (e: HttpException) {
                _error.value = "Error: ${e.code()} ${e.message()}"
            } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
                _token.value = null
        }
        }
    }



    fun fetchLoggedUser() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val usuario = useCase.getUserByToken(token)
                    useCase.insertUser(usuario)
                    _usuarioLogueado.value = usuario
                    _localUser.value = useCase.getLocalUserById(usuario.id)

                    // Crear usuario local si no existe
                    if (_localUser.value == null) {
                        crearUsuarioLocal(
                            id = usuario.id,
                            nombre = usuario.firstName,
                            apellido = usuario.lastName,
                            correo = usuario.email
                        )
                    }
                    Log.i("USUARIO", _usuarioLogueado.value.toString())
                } else {
                    Log.e("USUARIO", "No se pudo obtener el token de autenticación")
                }
            } catch (e: Exception) {
                // Intenta cargar el usuario desde la base de datos local
                try {
                    if (_localUser.value != null) {
                        val localUser = useCase.getLocalUser(_localUser.value!!.id)
                        _usuarioLogueado.value = localUser
                    }
                } catch (dbException: Exception) {
                    Log.e("USUARIO", "Error al obtener el usuario: ${e.message}")
                    _error.value = "Error al obtener el usuario: ${e.message}"
                }
            }
        }
    }

    fun updateUserProfileImage(user: Usuario, imgPerfil: String) {
        viewModelScope.launch {
            val updatedUser = user.copy(imgPerfil = imgPerfil)
            useCase.updateLocalUser(updatedUser)
            _localUser.value = updatedUser
        }
    }
    fun crearUsuarioLocal(id: Long, nombre: String, apellido: String, correo: String){
        viewModelScope.launch {
            try {
                val localUser = Usuario(
                    id = id,
                    nombre = nombre,
                    apellido = apellido,
                    email = correo,
                    imgPerfil = null )

                useCase.insertLocalUser(localUser)
                _localUser.value = localUser
            } catch (e: Exception) {
                Log.e("USUARIO", "Error al insertar usuario local: ${e.message}")
            }
        }
    }

    fun fetchLocalUser(userId: Long) {
        viewModelScope.launch {
            try {
                val user = useCase.getLocalUserById(userId)
                if (user != null) {
                    _localUser.value = user
                } else {
                    _error.value = "No se encontró el usuario en la base de datos local"
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener el usuario local: ${e.message}"
            }
        }
    }


}


