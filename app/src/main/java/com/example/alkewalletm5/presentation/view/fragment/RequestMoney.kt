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
import com.example.alkewalletm5.databinding.FragmentRequestMoneyBinding
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
 * Fragmento que representa la página de solicitud de dinero.
 */
class RequestMoney : Fragment() {

    private var _binding: FragmentRequestMoneyBinding? = null
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

    /**
     * Crea y retorna la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre que contiene la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     * @return La vista para la interfaz de usuario del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRequestMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Método llamado después de que la vista asociada con el fragmento haya sido creada.
     *
     * @param view La vista retornada por `onCreateView`.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinoViewModel.cargarUsuarios()
        userViewModel.fetchLoggedUser()
        accountViewModel.fetchUserAccounts()

        //Al presionar la flecha de la aprte superior vuelve al homePage
        binding.btnVolverRequest.setOnClickListener{findNavController().navigate(R.id.homePage)}

        //mostrarListadeDestinatarios()
        destinoViewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            mostrarListadeDestinatarios()
            Log.e("USUARIO", mostrarListadeDestinatarios().toString())
        }

        cambioColorTextViewMonto()

        binding.btnIngresarDinero.setOnClickListener() {

            val monto = binding.editTextMontoIngresarDinero.text.toString()
            val destinatario = binding.spinnerRecibirDinero.selectedItem as UserResponse
            val nota = binding.editTextNota.text.toString()
            val iconoRequest = R.drawable.request_icon2

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

            val montoRecibido: Double = monto.toDouble()

            //Verifica que cuando se solicita el dinero, este más el monto actual no supere el máximo permitido
            val saldoMaximo = accountViewModel.sumarSaldoUsuario(montoRecibido)
            if (!saldoMaximo) {
                Toast.makeText(
                    requireContext(),
                    "Excede el saldo máximo permitido de $5.000.000",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val user = userViewModel.usuarioLogueado.value

            // Obtener la cuenta del destinatario
            accountViewModel.accounts.observe(viewLifecycleOwner) { accounts ->
                if (accounts.isNotEmpty() && user != null) {
                    val userAccount = accounts[0]
                    transactionViewModel.createTransaction(montoRecibido.toLong(), nota, "rquestMoney",userAccount.id, user.id )
                    Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homePage)
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener la cuenta del usuario", Toast.LENGTH_SHORT).show()
                }
            }


            Toast.makeText(requireContext(), "Envío de dinero exitoso", Toast.LENGTH_SHORT).show()

            // Navegar de regreso a HomePage
            findNavController().navigate(R.id.homePage)

        }

    }

    fun mostrarListadeDestinatarios(){
        // Lista de elementos para el Spinner
        val spinner = binding.spinnerRecibirDinero

        destinoViewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            val adapter = DestinatarioAdpater(requireContext(), usuarios)
            spinner.adapter = adapter

        }
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

    /**
     * Método llamado cuando la vista asociada con el fragmento está siendo destruida.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}