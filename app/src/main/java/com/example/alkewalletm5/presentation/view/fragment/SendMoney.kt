package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Destinatario
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.databinding.FragmentSendMoneyBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.view.adapter.DestinatarioAdpater
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModel
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.DestinatarioViewModel
import com.example.alkewalletm5.presentation.viewmodel.DestinoViewModel
import com.example.alkewalletm5.presentation.viewmodel.DestinoViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
/**
 * Fragmento que representa la página de envío de dinero.
 */
class SendMoney : Fragment(){

    private var _binding: FragmentSendMoneyBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel
    private lateinit var destinoViewModel: DestinoViewModel

    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var destinoViewModelFactory: DestinoViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar los ViewModels aquí
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService)

        useCase = AlkeWalletUseCase(repository)
        destinoViewModelFactory = DestinoViewModelFactory(useCase, requireContext())
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())

        destinoViewModel = ViewModelProvider(this, destinoViewModelFactory).get(DestinoViewModel::class.java)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinoViewModel.cargarUsuarios()

        // Al presionar la flecha de la parte superior vuelve al homePage
        binding.volverSendMoney.setOnClickListener { findNavController().navigate(R.id.homePage) }

        destinoViewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            mostrarListadeDestinatarios()
            Log.e("USUARIO", mostrarListadeDestinatarios().toString())
        }

       // mostrarListadeDestinatarios()

        cambioColorTextViewMonto()

        binding.btnEnviarDinero.setOnClickListener {
            val monto = binding.editTextMontoEnviarDinero.text.toString()
            val destinatario = binding.spinnerEnviarDinero.selectedItem as UserResponse
            val nota = binding.editTextNotaEnviarDinero.text.toString()
            val iconoSend = R.drawable.send_icon_yellow

            // No permite enviar dinero si no se han llenado los campos
            if (monto.isBlank()) {
                Toast.makeText(requireContext(), "Por favor ingrese un monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nota.isBlank()) {
                Toast.makeText(requireContext(), "Por favor ingrese una nota", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (destinatario !is UserResponse) {
                Toast.makeText(requireContext(), "Por favor seleccione un destinatario válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (destinatario == null) {
                Toast.makeText(requireContext(), "Por favor seleccione un destinatario válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

            // Navegar de regreso a HomePage
            findNavController().navigate(R.id.homePage)
        }


    }

    fun mostrarListadeDestinatarios(){
        // Lista de elementos para el Spinner
        val spinner = binding.spinnerEnviarDinero

        destinoViewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            val adapter = DestinatarioAdpater(requireContext(), usuarios)
            spinner.adapter = adapter

        }
    }

    fun obtenerFecha(): String{
        // Obtener la fecha y hora actual
        val calendar = Calendar.getInstance()

        // Formatear la fecha y hora según el formato deseado
        val dateFormat = SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun cambioColorTextViewMonto(){
        val editTextMonto = binding.editTextMontoEnviarDinero

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
                    editTextMonto.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde))
                    editTextMonto.setBackgroundResource(R.drawable.edittextt_background_verde)
                }
            }
        })
    }

}

//val montoEnviado: Double = monto.toDouble()

// Verificar si el saldo es suficiente antes de realizar la transacción
/*val saldoSuficiente = usuarioViewModel.restarSaldoUsuario(montoEnviado)
if (!saldoSuficiente) {
    Toast.makeText(requireContext(), "Saldo insuficiente para realizar la transacción", Toast.LENGTH_SHORT).show()
    return@setOnClickListener
}*/

// Creamos una nueva transacción
/*transaccionViewModel.nuevaTransaccion(
    fotoPerfil = destinatario.fotoPerfil,
    idReceiver = destinatario.nombre,
    monto = montoEnviado,
    icono = iconoSend,
    fecha = obtenerFecha(),
    simbolo = "-$"
)

// Añadir la nueva transacción a la lista de transacciones
transaccionViewModel.addTransaccion()*/