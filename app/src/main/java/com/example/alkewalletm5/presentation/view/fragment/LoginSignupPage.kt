package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
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
        // Inflate the layout for this fragment
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