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

class SignupPage : Fragment() {
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
        return inflater.inflate(R.layout.fragment_signup_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController(view)
        val btnLogin = view.findViewById<Button>(R.id.buttonLoginSignup)
        val btnNuevaCuenta = view.findViewById<TextView>(R.id.enlaceYaTieneCuentaSignup)
        btnLogin.setOnClickListener { v: View? -> navController.navigate(R.id.homePage) }
        btnNuevaCuenta.setOnClickListener { v: View? -> navController.navigate(R.id.loginPage) }
    }
}