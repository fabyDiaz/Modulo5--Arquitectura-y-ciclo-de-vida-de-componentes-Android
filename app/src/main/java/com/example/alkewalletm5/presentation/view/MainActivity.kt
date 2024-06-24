package com.example.alkewalletm5.presentation.view
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v2.0 20/06/2024
 *
 */
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.databse.AppDataBase
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.api.AuthManager
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.UserViewModelFactory

/**
 * Actividad principal de la aplicación que se lanza al iniciar.
 *
 * Esta actividad es responsable de cargar el diseño principal de la aplicación.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var userViewModel: UserViewModel
    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory


    /**
     * Método llamado cuando la actividad es creada por primera vez.
     *
     * @param savedInstanceState Si no es nulo, este objeto contiene el estado de la actividad guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(R.layout.activity_main)

        // Inicializa AuthManager
        authManager = AuthManager(this)
        val database = AppDataBase.getDatabase(this)
        val walletDao = database.WalletDao()
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)
        useCase = AlkeWalletUseCase(repository)
        userViewModelFactory = UserViewModelFactory(useCase, this)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        // Obtiene el token
        val token = authManager.getToken()
        if (token != null) {
            Log.d("TOKENMAIN", token)
            // Aquí puedes realizar acciones adicionales si el token existe
        } else {
            // El token no existe, redirige a la página de login o realiza otras acciones
        }
    }

    override fun onStop() {
        super.onStop()
        authManager.clearToken()
        authManager.clearUserLogged()
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.clearToken()
        authManager.clearUserLogged()
    }

    fun refreshUserData() {
        userViewModel.fetchLoggedUser()
    }
}