package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.Navigation.findNavController
import com.example.alkewalletm5.R


class HomePage : Fragment() {
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
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController(view)
        val btnInresarDinero = view.findViewById<Button>(R.id.btnIngresarDineroHome)
        val btnEnviarDinero = view.findViewById<Button>(R.id.btnEnviarDineroHome)
        val btnPerfil = view.findViewById<ImageView>(R.id.imagenHomeAmanda)
        btnInresarDinero.setOnClickListener { v: View? -> navController.navigate(R.id.requestMoney) }
        btnEnviarDinero.setOnClickListener { v: View? -> navController.navigate(R.id.sendMoney) }
        btnPerfil.setOnClickListener { v: View? -> navController.navigate(R.id.profilePage) }
    }
}