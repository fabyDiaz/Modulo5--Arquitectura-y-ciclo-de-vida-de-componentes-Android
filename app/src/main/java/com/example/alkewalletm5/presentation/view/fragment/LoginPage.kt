package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.databinding.FragmentLoginPageBinding

class LoginPage : Fragment() {

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
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
        var txtEmail = binding.editTextEmail.text.toString()
        var txtPassword = binding.editTextPasswordLogin.text.toString()

        if(txtEmail=="Amanda@gmail.com" && txtPassword=="amanda123"){
            findNavController().navigate(R.id.action_loginPage_to_homePage)
        }else{
            Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }

    }


}