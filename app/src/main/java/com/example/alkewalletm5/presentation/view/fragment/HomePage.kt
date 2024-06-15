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
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.databinding.FragmentHomePageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.view.adapter.TransaccionAdapter
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.UserViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

/**
 * Fragmento que representa la página principal de la aplicación.
 */
class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var adapter: TransaccionAdapter
    //private val usuarioViewModel: UsuarioViewModel by activityViewModels()
    private lateinit var userViewModel: UserViewModel
    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory

    private val transaccionViewModel: TransaccionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Crea y retorna la jerarquía de vistas asociada con el fragmento.
     *
     * @param inflater El LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el padre que contiene la vista del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un estado previamente guardado.
     * @return La vista para la interfaz de usuario del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        // Manejar el botón de retroceso del dispositivo
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navegar de vuelta a Fragment1
                //findNavController().navigate(R.id.loginSignupPage)
                mostrarCuadroDialogo()
            }
        })

        return binding.root
    }

    /**
     * @param view La vista retornada por `onCreateView`.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo recreado a partir de un
     * estado previamente guardado.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(view)

        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService)

        useCase = AlkeWalletUseCase(repository)
        userViewModelFactory = UserViewModelFactory(useCase,  requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        //Navegación de lo botones de la aplicación
        binding.btnIngresarDineroHome.setOnClickListener { navController.navigate(R.id.requestMoney) }
        binding.btnEnviarDineroHome.setOnClickListener { navController.navigate(R.id.sendMoney) }
        binding.imagenHomeAmanda.setOnClickListener { navController.navigate(R.id.profilePage) }

        //Intanciamos el adapter
        binding.recyclerTransacciones.layoutManager = LinearLayoutManager(context)
        adapter = TransaccionAdapter()
        binding.recyclerTransacciones.adapter = adapter

        //Actualiza el recyclerview
        transaccionViewModel.transacciones.observe(viewLifecycleOwner) { transacciones ->
            adapter.items = transacciones
            adapter.notifyDataSetChanged()
            updateEmptyState()
        }

        userViewModel.fetchLoggedUser()
        //Actualiza cabecera del home
        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                Log.e("USUARIO", usuario.toString())
                binding.textSaludo.text = "Hola, ${usuario.firstName}"
            }
        }

        updateEmptyState()
    }

    /**
     * Actualiza el estado vacío de la lista de transacciones.
     * Si hay transacciones muestra la lista de transacciones, sino la vista que dice que no hay
     * transaciones
     */
    private fun updateEmptyState() {
        if (adapter.items.isEmpty()) {
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
