package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.databinding.FragmentLoginPageBinding
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel

class LoginPage : Fragment() {

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root
        // Navegar de vuelta al loginSignupPage
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

    }

    /**
     * Este método verifica si el usuario ingresado desde el LoginPage con su email y su contraseña
     * existe en la lista de usuarios. Si lo encuentra lo agrega a la lista de usuarioLogueado y
     * luego entra al HomePage
     */
   fun VerificarEmailPassword(){

       val txtEmail = binding.editTextEmail.text.toString()
       val txtPassword = binding.editTextPasswordLogin.text.toString()

       val usuario = usuarioViewModel.autenticarUsuario(txtEmail, txtPassword)

       if (usuario != null) {
           usuarioViewModel.setUsuarioLogueado(usuario)
           findNavController().navigate(R.id.homePage)
       } else {
           Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
       }
   }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }


}