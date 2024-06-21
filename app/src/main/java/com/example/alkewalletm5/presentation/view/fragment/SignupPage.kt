package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v2.0 20/06/2024
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
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.databinding.FragmentSignupPageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.UserViewModelFactory

class SignupPage : Fragment() {

    private var _binding: FragmentSignupPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var userViewModelFactory: UserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDataBase.getDatabase(requireContext())
        val walletDao = database.WalletDao()

        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)
        val useCase = AlkeWalletUseCase(repository)

        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupPageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Navegar de vuelta a Fragment1
                    findNavController().navigate(R.id.loginSignupPage)
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(view)
        binding.buttonLoginSignup.setOnClickListener { llenarFormularioSignup() }
        binding.enlaceYaTieneCuentaSignup.setOnClickListener { navController.navigate(R.id.loginPage) }

        userViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                Log.e("ERROR", it)
            }
        }

        userViewModel.createdUserId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.loginPage)
            } else {
                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun llenarFormularioSignup() {
        val nombre = binding.inputNombre.text.toString()
        val apellido = binding.inputApellidoSignup.text.toString()
        val email = binding.inputEmailSignup.text.toString()
        val password = binding.inputPasswordSignup.text.toString()
        val confirmPassword = binding.inputPassword2Signup.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Por favor complete todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val newUser = UserResponse(
            id = 0,
            firstName = nombre,
            lastName = apellido,
            email = email,
            password = password,
            points = 0,
            roleId = 1
        )

        userViewModel.createUser(newUser)

        Log.d("USUARIO", "Usuario enviado a la API: " + userViewModel.user.toString())

        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.loginPage)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}