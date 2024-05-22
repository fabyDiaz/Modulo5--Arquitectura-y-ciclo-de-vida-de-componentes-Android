package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Destinatario
import com.example.alkewalletm5.databinding.FragmentRequestMoneyBinding
import com.example.alkewalletm5.presentation.view.adapter.DestinatarioAdpater
import com.example.alkewalletm5.presentation.viewmodel.DestinatarioViewModel
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RequestMoney : Fragment() {

    private var _binding: FragmentRequestMoneyBinding? = null
    private val binding get() = _binding!!
    private val transaccionViewModel: TransaccionViewModel by activityViewModels()
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private val destinatarioViewModel: DestinatarioViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRequestMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Al presionar la flecha de la aprte superior vuelve al homePage
        binding.btnVolverRequest.setOnClickListener{findNavController().navigate(R.id.homePage)}

        mostrarListadeDestinatarios()

        cambioColorTextViewMonto()

        binding.btnIngresarDinero.setOnClickListener() {

            val monto = binding.editTextMontoIngresarDinero.text
            val destinatario = binding.spinnerRecibirDinero.selectedItem as Destinatario
            val nota = binding.editTextNota.text
            val iconoRequest = R.drawable.request_icon2
            val montoRecibido: Double = monto.toString().toDouble()

            //No permite Recibir dinero si no se han llenado los campos
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

            transaccionViewModel.nuevaTransaccion(
                fotoPerfil = destinatario.fotoPerfil,
                idReceiver = destinatario.nombre,
                monto = montoRecibido,
                icono = iconoRequest,
                fecha = obtenerFecha()
            )


            // Añadir la nueva transacción al ViewModel compartido
            transaccionViewModel.addTransaccion()
            usuarioViewModel.sumarSaldoUsuario(montoRecibido)

            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

            // Navegar de regreso a HomePage
            findNavController().navigate(R.id.homePage)

        }

    }

    /**
     *Se obtiene una referencia al spinner desde el layout usando la propiedad binding.spinnerEnviarDinero
     * observa el LiveData destinatarioViewModel.destinatarios utilizando viewLifecycleOwner
     * Cuando el LiveData de destinatarios cambia, se ejecuta el bloque de código dentro del lambda.
     * Dentro de este bloque, se crea un adaptador (DestinatarioAdapter) utilizando la lista actualizada de
     * destinatarios y se establece este adaptador en el spinner.
     */
    fun mostrarListadeDestinatarios(){
        val spinner = binding.spinnerRecibirDinero

        // Lista de elementos para el Spinner
        destinatarioViewModel.destinatarios.observe(viewLifecycleOwner) { destinatarios ->
            // Adapter para el Spinner
            val adapter = DestinatarioAdpater(requireContext(), destinatarios)
            spinner.adapter = adapter
        }
    }

    /**
     * Obtener la fecha actual y retornarla como un String
     * Se obtiene una instancia de Calendar que representa la fecha y hora actuales.
     * Se crea un objeto SimpleDateFormat, que permite formatear fechas y horas
     * @return La fecha y hora actual formateada como una cadena de texto en el formato "MMM d, hh:mm a"
     */
    fun obtenerFecha(): String{
        // Obtener la fecha y hora actual
        val calendar = Calendar.getInstance()

        // Formatear la fecha y hora según el formato deseado
        val dateFormat = SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    /**
     * Esta función pinta el editText del monto
     * cuando no se ha ingresado un monto, está en gris
     * Cuando se empieza a escribir se pone azul
     */
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}