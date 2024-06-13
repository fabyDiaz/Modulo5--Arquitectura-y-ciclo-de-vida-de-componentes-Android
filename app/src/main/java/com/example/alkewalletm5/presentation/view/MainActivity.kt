package com.example.alkewalletm5.presentation.view
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.network.api.AuthManager

/**
 * Actividad principal de la aplicación que se lanza al iniciar.
 *
 * Esta actividad es responsable de cargar el diseño principal de la aplicación.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager

    /**
     * Método llamado cuando la actividad es creada por primera vez.
     *
     * @param savedInstanceState Si no es nulo, este objeto contiene el estado de la actividad guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Inicializa AuthManager
        authManager = AuthManager(this)

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
}