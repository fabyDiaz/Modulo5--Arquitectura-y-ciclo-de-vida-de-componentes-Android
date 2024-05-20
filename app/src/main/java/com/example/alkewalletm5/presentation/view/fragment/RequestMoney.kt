package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.DestinatariosDataSet
import com.example.alkewalletm5.data.model.Destinatario
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.databinding.FragmentRequestMoneyBinding
import com.example.alkewalletm5.databinding.FragmentSendMoneyBinding
import com.example.alkewalletm5.presentation.view.adapter.DestinatarioAdpater
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
import com.google.android.material.appbar.MaterialToolbar

class RequestMoney : Fragment() {

    private lateinit var binding: FragmentRequestMoneyBinding
    private val transaccionViewModel: TransaccionViewModel by activityViewModels()
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
        binding = FragmentRequestMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flecha = view.findViewById<MaterialToolbar>(R.id.btnVolverRequest)
        flecha.setOnClickListener { v: View? ->
            findNavController(
                v!!
            ).navigate(R.id.homePage)
        }

        val spinner = view.findViewById<Spinner>(R.id.spinnerRecibirDinero)

        // Lista de elementos para el Spinner
        val items = DestinatariosDataSet().ListaDestintarios()

        // Adapter para el Spinner
        val adapter = DestinatarioAdpater(requireContext(), items)
        spinner.adapter = adapter

        binding = FragmentRequestMoneyBinding.bind(view)

       cambioColorTextViewMonto()

        binding.btnIngresarDinero.setOnClickListener() {

            val monto = binding.editTextMontoIngresarDinero.text
            val destinatario = binding.spinnerRecibirDinero.selectedItem as Destinatario
            val nota = binding.editTextNota.text
            if (monto.isBlank()) {
                Toast.makeText(requireContext(), "Por favor ingrese un monto", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (nota.isBlank()) {
                Toast.makeText(requireContext(), "Por favor ingrese una nota", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val montoRecibido: Double = monto.toString().toDouble()
            // Verificar si el saldo es suficiente antes de realizar la transacción

            val nuevaTransaccion = Transaccion(
                id="5",
                fotoPerfil = destinatario.fotoPerfil,
                idSender = destinatario.nombre,
                monto=montoRecibido )

            // Añadir la nueva transacción al ViewModel compartido
            transaccionViewModel.addTransaccion(nuevaTransaccion)

            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

            // Navegar de regreso a HomePage
            findNavController().navigate(R.id.homePage)

        }

    }

    fun cambioColorTextViewMonto(){
        val editTextMonto = binding.editTextMontoIngresarDinero

        // Configurar el TextWatcher para cambiar el color del texto y el borde
        editTextMonto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementación
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se necesita implementación
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editTextMonto.setTextColor(ContextCompat.getColor(requireContext(), R.color.semiblack))
                    editTextMonto.setBackgroundResource(R.drawable.edittext_backgraound_gris)
                } else {
                    editTextMonto.setTextColor(ContextCompat.getColor(requireContext(), R.color.azul))
                    editTextMonto.setBackgroundResource(R.drawable.edittext_background_azul)
                }
            }
        })
    }
}