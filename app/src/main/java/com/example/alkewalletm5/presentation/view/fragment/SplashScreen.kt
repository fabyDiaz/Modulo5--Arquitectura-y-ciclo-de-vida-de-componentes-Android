package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel

/**
 * Fragmento que representa la pantalla de carga inicial de la aplicación.
 */
class SplashScreen : Fragment() {

    private val SPLASH_SCREEN_DURATION = 1000L
    private lateinit var userViewModel: UserViewModel
    private lateinit var userViewModelFactory: UserViewModelFactory
    val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
    val repository = AlkeWalletImpl(apiService)
    val useCase =AlkeWalletUseCase(repository)

    /**
     * Método llamado para hacer la inicialización inicial del fragmento.
     *
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }


    /**
     * Crea y retorna la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre que contiene la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un estado previamente guardado.
     * @return La vista para la interfaz de usuario del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
    /**
     * Método llamado después de que la vista asociada con el fragmento haya sido creada.
     *
     * @param view La vista retornada por `onCreateView`.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un estado previamente guardado.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModelFactory = UserViewModelFactory(useCase)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)


        val userId = 5L // Asume que tienes el ID del usuario


        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            // Registra la información del usuario en los logs
            Log.d("UserFragment", "Usuario recibido: ${user.firstName} ${user.lastName}")
        })

        userViewModel.getUserById(5)


       //  Retraso antes de navegar al siguiente fragmento
        Handler(Looper.getMainLooper()).postDelayed({
            // Navegar al siguiente destino después del tiempo deseado
            findNavController().navigate(R.id.action_spashScreen_to_loginSignupPage)
        }, SPLASH_SCREEN_DURATION)


    }
}