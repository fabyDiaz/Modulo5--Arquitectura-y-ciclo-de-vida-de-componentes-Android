package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.alkewalletm5.data.local.TransaccionesDataSet
import com.example.alkewalletm5.data.model.Destinatarios
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.databinding.FragmentSendMoneyBinding
import com.example.alkewalletm5.presentation.view.adapter.DestinatarioAdpater
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.google.android.material.appbar.MaterialToolbar

class SendMoney : Fragment() {

    private lateinit var binding: FragmentSendMoneyBinding
    private val transaccionViewModel: TransaccionViewModel by activityViewModels()
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
        binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val flecha = view.findViewById<MaterialToolbar>(R.id.volverSendMoney)

        flecha.setOnClickListener { v: View? -> findNavController(v!!).navigate(R.id.homePage)
        }

        val spinner = view.findViewById<Spinner>(R.id.spinnerEnviarDinero)

        // Lista de elementos para el Spinner
        val items = listOf(
            Destinatarios(R.mipmap.pp_standar, "Amanda", "Amanda@gmail.com"),
            Destinatarios(R.mipmap.pp1, "John Doe", "john.doe@example.com"),
            Destinatarios(R.mipmap.pp2, "Jane Smith", "jane.smith@example.com")
        )

        // Adapter para el Spinner
        val adapter = DestinatarioAdpater(requireContext(), items)
        spinner.adapter = adapter

        binding = FragmentSendMoneyBinding.bind(view)
        val monto = binding.editTextMontoEnviarDinero.text
        val nota = binding.editTextNotaEnviarDinero.text
        val editTextMonto = binding.editTextMontoEnviarDinero
        val destinatario = binding.spinnerEnviarDinero.selectedItem as Destinatarios

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
                } else {
                    editTextMonto.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde))
                }
            }
        })



        binding.btnEnviarDinero.setOnClickListener() {

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

            // Si el monto y la nota no están vacíos, mostrar el mensaje de éxito
            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()
            val montoEnviado: Double = monto.toString().toDouble()

            val nuevaTransaccion = Transaccion(
                id="5",
                fotoPerfil = destinatario.fotoPerfil,
                idReceriver = destinatario.nombre,
                monto=montoEnviado )

            // Añadir la nueva transacción al ViewModel compartido
            transaccionViewModel.addTransaccion(nuevaTransaccion)
            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

            // Navegar de regreso a HomePage
            findNavController().navigate(R.id.homePage)

        }


    }

}