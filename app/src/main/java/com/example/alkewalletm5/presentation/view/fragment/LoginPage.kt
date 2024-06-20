package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.databse.AppDataBase
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.databinding.FragmentLoginPageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory

/**
 * Fragmento que representa la página de inicio de sesión.
 */
class LoginPage : Fragment() {

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDataBase.getDatabase(requireContext())
        val walletDao = database.WalletDao()
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)

        useCase = AlkeWalletUseCase(repository)
        userViewModelFactory = UserViewModelFactory(useCase,  requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.loginSignupPage)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navController = findNavController(view)

        binding.buttonLoginLogin.setOnClickListener { VerificarEmailPassword() }
        binding.enlaceCrearCuentaLogin.setOnClickListener { navController.navigate(R.id.signupPage) }

        // Observa cambios en el token para verificar si se recibe correctamente
        userViewModel.token.observe(viewLifecycleOwner) { token ->
            if (token != null) {
                Toast.makeText(requireContext(), "Login exitoso. Token: $token", Toast.LENGTH_SHORT).show()
                Log.e("TOKEN", token.toString())
                userViewModel.fetchLoggedUser() // Fetch user data
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observa cambios en el usuario logueado para navegar al home
        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            Log.d("USUARIO", usuario.firstName)
            if (usuario != null) {
                findNavController().navigate(R.id.homePage)
            }
        }
    }

    fun VerificarEmailPassword() {
        val txtEmail = binding.editTextEmail.text.toString()
        val txtPassword = binding.editTextPasswordLogin.text.toString()
        userViewModel.login(txtEmail, txtPassword)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }

}