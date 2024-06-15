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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Usuario
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.databinding.FragmentSignupPageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
/**
 * Fragmento que representa la página de registro de usuario.
 */
class SignupPage : Fragment() {

    private var _binding: FragmentSignupPageBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var userViewModel: UserViewModel
    private lateinit var userViewModelFactory: UserViewModelFactory

    val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
    val repository = AlkeWalletImpl(apiService)
    val useCase = AlkeWalletUseCase(repository)

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
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     * @return La vista para la interfaz de usuario del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupPageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar de vuelta a Fragment1
                findNavController().navigate(R.id.loginSignupPage)
            }
        })
        return binding.root
    }
    /**
     * Método llamado después de que la vista asociada con el fragmento haya sido creada.
     *
     * @param view La vista retornada por `onCreateView`.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(view)
        binding.buttonLoginSignup.setOnClickListener{llenarFormularioSignup()}
        binding.enlaceYaTieneCuentaSignup.setOnClickListener{navController.navigate(R.id.loginPage)}


        userViewModelFactory = UserViewModelFactory(useCase,  requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->

        })

       userViewModel.createdUserId.observe(viewLifecycleOwner, Observer { userId ->
            Log.d("TOKEN", "ID del usuario creado: $userId")

        })

    }

    /**
     * Verifica y llena el formulario de registro.
     * Realiza validaciones de los campos y agrega un nuevo usuario si todas las validaciones pasan.
     */
    private fun llenarFormularioSignup() {
        val nombre = binding.inputNombre.text.toString()
        val apellido = binding.inputApellidoSignup.text.toString()
        val email = binding.inputEmailSignup.text.toString()
        val password = binding.inputPasswordSignup.text.toString()
        val confirmPassword = binding.inputPassword2Signup.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        val newUser = UserResponse(
            id = 0,
            firstName = nombre,
            lastName = apellido,
            email = email,
            password = password,
            points = 0,
            roleId = 1, // Debes establecer el roleId apropiado aquí
            createdAt = "", // Este valor probablemente será establecido por el servidor
            updatedAt = "" // Este valor probablemente será establecido por el servidor
        )

        userViewModel.createUserAndGetId(newUser)

        Log.d("USUARIO", "Usuario enviado a la API: "+userViewModel.user.toString())
        userViewModel.createdUserId.observe(viewLifecycleOwner, Observer { userId ->
            Log.d("USUARIO", "ID del usuario creado: $userId")
        })

        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.homePage)
    }
    /**
     * Método llamado cuando la vista asociada con el fragmento está siendo destruida.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}