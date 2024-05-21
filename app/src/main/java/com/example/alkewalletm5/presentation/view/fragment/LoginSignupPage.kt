package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R

class LoginSignupPage : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Tengo que buscar la forma de salir de la app
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar de vuelta a Fragment1
                findNavController().navigate(R.id.loginSignupPage)
            }
        })
        return inflater.inflate(R.layout.fragment_login_signup_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController(view)
        val btnCrearCuenta = view.findViewById<Button>(R.id.btnCrearCuenta)
        val btnCuentaCreada = view.findViewById<TextView>(R.id.enlaceYaTienesCuenta)
        btnCrearCuenta.setOnClickListener { v: View? -> navController.navigate(R.id.signupPage) }
        btnCuentaCreada.setOnClickListener { v: View? -> navController.navigate(R.id.loginPage) }
    }
}