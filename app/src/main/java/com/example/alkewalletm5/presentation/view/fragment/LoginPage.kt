package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.databinding.FragmentLoginPageBinding
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel

class LoginPage : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding

    /*private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!*/

    private val usuarioViewModel: UsuarioViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController(view)
        val btnLogin = view.findViewById<Button>(R.id.buttonLoginLogin)
        val btnNuevaCuenta = view.findViewById<TextView>(R.id.enlaceCrearCuentaLogin)
        //btnLogin.setOnClickListener { v: View? -> navController.navigate(R.id.homePage) }
        btnLogin.setOnClickListener { VerificarEmailPassword() }
        btnNuevaCuenta.setOnClickListener { v: View? -> navController.navigate(R.id.signupPage) }
    }

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


}