package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.databinding.FragmentProfilePageBinding
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel

class ProfilePage : Fragment() {
    private lateinit var binding: FragmentProfilePageBinding
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
        // Inflate the layout for this fragment
        binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar de vuelta a Fragment1
                findNavController().navigate(R.id.homePage)
            }
        })
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Actualiza cabecera del home
        usuarioViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            binding.textNombrePerfil.text = usuario.nombre
            binding.imagenFotoPerfil.setImageResource(usuario.imgPerfil)
        }

        usuarioViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            binding.txtMostrarInformaccion.text = "Nombre: ${usuario.nombre} \n" +
                                                    "Apellido: ${usuario.apellido} \n" +
                                                        "Email: ${usuario.email} \n"
            binding.txtMostrarTarjetas.text = "No tiene tarjetas asociadas"
        }

        binding.tarjetaMiInformacion.setOnClickListener{
            cambiarVisibilidadCard(binding.infoContainerMiInformacion)
        }
        binding.tarjetaMisTarjetas.setOnClickListener{
            cambiarVisibilidadCard(binding.infoContainerMiMistarjetas)
        }


    }

    private fun cambiarVisibilidadCard(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }




}