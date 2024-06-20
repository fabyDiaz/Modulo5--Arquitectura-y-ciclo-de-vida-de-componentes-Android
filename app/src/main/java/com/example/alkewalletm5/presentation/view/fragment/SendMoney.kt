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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.databse.AppDataBase
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.data.response.UserResponse
import com.example.alkewalletm5.databinding.FragmentSendMoneyBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.view.adapter.DestinatarioAdpater
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModel
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.DestinoViewModel
import com.example.alkewalletm5.presentation.viewmodel.DestinoViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.TransactionViewModel
import com.example.alkewalletm5.presentation.viewmodel.TransactionViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory
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
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var destinoViewModelFactory: DestinoViewModelFactory
    private lateinit var accountViewModelFactory: AccountViewModelFactory
    private lateinit var transactionViewModelFactory: TransactionViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDataBase.getDatabase(requireContext())
        val walletDao = database.WalletDao()
        // Inicializar los ViewModels aquí
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)

        useCase = AlkeWalletUseCase(repository)
        destinoViewModelFactory = DestinoViewModelFactory(useCase, requireContext())
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        accountViewModelFactory = AccountViewModelFactory(useCase, requireContext())
        transactionViewModelFactory = TransactionViewModelFactory(useCase, requireContext())

        destinoViewModel = ViewModelProvider(this, destinoViewModelFactory).get(DestinoViewModel::class.java)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        transactionViewModel = ViewModelProvider(this, transactionViewModelFactory).get(TransactionViewModel::class.java)
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
        userViewModel.fetchLoggedUser()

        // Al presionar la flecha de la parte superior vuelve al homePage
        binding.volverSendMoney.setOnClickListener { findNavController().navigate(R.id.homePage) }

        accountViewModel.fetchUserAccounts()

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

            if (destinatario !is UserResponse || destinatario == null) {
                Toast.makeText(requireContext(), "Por favor seleccione un destinatario válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val montoEnviado: Double = monto.toDouble()
            val saldoSuficiente = accountViewModel.restarSaldoUsuario(montoEnviado)
            if (!saldoSuficiente) {
                Toast.makeText(requireContext(), "Saldo insuficiente para realizar la transacción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = userViewModel.usuarioLogueado.value

            // Obtener la cuenta del destinatario
            accountViewModel.accounts.observe(viewLifecycleOwner) { accounts ->
                if (accounts.isNotEmpty() && user != null) {
                    val userAccount = accounts[0]
                    transactionViewModel.createTransaction(montoEnviado.toLong(), nota, "sendMoney", userAccount.id, user.id)
                    Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homePage)
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener la cuenta del usuario", Toast.LENGTH_SHORT).show()
                }
            }

          //  Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

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

