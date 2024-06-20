package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.databse.AppDataBase
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.databinding.FragmentHomePageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.view.adapter.TransactionAdapter
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.AccountViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.TransactionViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.TransactionViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.UserViewModelFactory

/**
 * Fragmento que representa la página principal de la aplicación.
 */
class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var adapter: TransactionAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var transactionViewModel: TransactionViewModel
   // private lateinit var destinoViewModel: DestinoViewModel

    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var accountViewModelFactory: AccountViewModelFactory
    private lateinit var transactionViewModelFactory: TransactionViewModelFactory
  //  private lateinit var destinoViewModelFactory: DestinoViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDataBase.getDatabase(requireContext())
        val walletDao = database.WalletDao()
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)

        useCase = AlkeWalletUseCase(repository)
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        accountViewModelFactory = AccountViewModelFactory(useCase, requireContext())
        transactionViewModelFactory = TransactionViewModelFactory(useCase, requireContext())
        // destinoViewModelFactory = DestinoViewModelFactory(useCase, requireContext())

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        transactionViewModel = ViewModelProvider(this, transactionViewModelFactory).get(TransactionViewModel::class.java)
        //  destinoViewModel = ViewModelProvider(this, destinoViewModelFactory).get(DestinoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarCuadroDialogo()
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(view)

        binding.btnIngresarDineroHome.setOnClickListener { navController.navigate(R.id.requestMoney) }
        binding.btnEnviarDineroHome.setOnClickListener { navController.navigate(R.id.sendMoney) }
        binding.imagenHomeAmanda.setOnClickListener { navController.navigate(R.id.profilePage) }

        binding.recyclerTransacciones.layoutManager = LinearLayoutManager(context)

        userViewModel.fetchLoggedUser()
        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                binding.textSaludo.text = "Hola, ${usuario.firstName}"
                accountViewModel.fetchUserAccounts()
                //Log.d("USUARIO", "la transacciones que tiene: " + transactionViewModel.transactions.value.toString())
            }
        }

        accountViewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            if (accounts != null && accounts.isNotEmpty()) {
                binding.textMontoTotal.text = "$"+ accounts[0].money
            } else {
                binding.textMontoTotal.text = "0"
            }
        }

        transactionViewModel.transactions.observe(viewLifecycleOwner) { transactionsListResponse ->
            transactionsListResponse?.let { transactions ->
                if (transactions.data.isNotEmpty()) {
                    Log.d("USUARIO", "RecyclerViwe"+transactions.data.get(0).amount.toString())
                    adapter =TransactionAdapter(transactions.data)  // Inicializar el adaptador aquí
                    binding.recyclerTransacciones.adapter = adapter
                    Log.d("USUARIO", "Muestra el adaptador"+ adapter.toString())
                    adapter.notifyDataSetChanged()
                } else {
                    // Manejar el caso de que no haya transacciones
                    updateEmptyState()
                }
            }
        }

        transactionViewModel.getAlltransactions()

    }


    private fun updateEmptyState() {
        // Verificar si el adaptador está inicializado y tiene elementos
        if (::adapter.isInitialized && adapter.itemCount == 0) {
            binding.layoutTransaccionesEmpty.visibility = View.VISIBLE
            binding.recyclerTransacciones.visibility = View.GONE
        } else {
            binding.layoutTransaccionesEmpty.visibility = View.GONE
            binding.recyclerTransacciones.visibility = View.VISIBLE
        }
    }

    private fun mostrarCuadroDialogo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("¿Está seguro que desea salir?")
            .setPositiveButton("Sí") { dialog, id ->
                // Navegar de vuelta a loginSignupPage
                findNavController().navigate(R.id.loginSignupPage)
            }
            .setNegativeButton("No") { dialog, id ->
                // Cerrar el diálogo
                dialog.dismiss()
            }
        // Mostrar el diálogo
        builder.create().show()
    }


}
