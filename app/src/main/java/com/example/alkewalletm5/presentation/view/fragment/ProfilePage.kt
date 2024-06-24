package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v2.0 20/06/2024
 *
 */

import android.net.Uri
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.databse.AppDataBase
import com.example.alkewalletm5.data.network.api.AlkeWalletService
import com.example.alkewalletm5.data.network.retrofit.RetrofitHelper
import com.example.alkewalletm5.data.repository.AlkeWalletImpl
import com.example.alkewalletm5.databinding.FragmentProfilePageBinding
import com.example.alkewalletm5.domain.AlkeWalletUseCase
import com.example.alkewalletm5.presentation.view.MainActivity
import com.example.alkewalletm5.presentation.viewmodel.AccountViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.AccountViewModelFactory
import com.example.alkewalletm5.presentation.viewmodel.UserViewModel
import com.example.alkewalletm5.presentation.viewmodel.factory.UserViewModelFactory
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Fragmento que representa la página de perfil del usuario.
 */
class ProfilePage : Fragment() {
    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
   // private val usuarioViewModel: UsuarioViewModel by activityViewModels()


    private lateinit var userViewModel: UserViewModel
    private lateinit var useCase: AlkeWalletUseCase
    private lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var accountViewModelFactory: AccountViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDataBase.getDatabase(requireContext())
        val walletDao = database.WalletDao()
        val apiService = RetrofitHelper.getRetrofit().create(AlkeWalletService::class.java)
        val repository = AlkeWalletImpl(apiService, walletDao)

        useCase = AlkeWalletUseCase(repository)
        userViewModelFactory = UserViewModelFactory(useCase, requireContext())
        accountViewModelFactory = AccountViewModelFactory(useCase, requireContext())
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        accountViewModel = ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homePage)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.fetchLoggedUser()

        userViewModel.usuarioLogueado.observe(viewLifecycleOwner) {
            binding.textNombrePerfil.text = it.firstName
            binding.txtMostrarInformaccion.text = "Nombre: ${it.firstName} \n" +
                    "Apellido: ${it.lastName} \n" +
                    "Email: ${it.email} \n"

            userViewModel.localUser.observe(viewLifecycleOwner) { localUser ->
                if (localUser?.imgPerfil != null) {
                    Picasso.get()
                        .load(localUser.imgPerfil)
                        .centerCrop()
                        .fit()
                        .into(binding.imagenFotoPerfil)
                  //  binding.imagenFotoPerfil.setImageURI(Uri.parse(localUser.imgPerfil))
                }
            }

            accountViewModel.obtenerUserAccounts(it.id)
        }

        accountViewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            if (accounts.isNullOrEmpty()) {
                binding.txtMostrarTarjetas.text = "No tiene tarjetas asociadas"
                binding.btnCrearCuenta.visibility = View.VISIBLE
            } else {
                binding.txtMostrarTarjetas.text = accounts.joinToString(separator = "\n\n") { account ->
                    "ID: ${account.id} \n" +
                            "Fecha de creación: ${convertirFecha(account.creationDate)}\n" +
                            "Saldo: ${account.money}"
                }
                binding.btnCrearCuenta.visibility = View.GONE
            }
        }

        accountViewModel.account.observe(viewLifecycleOwner) { account ->
           account?.let {
               binding.txtMostrarTarjetas.text = "NUEVA CUENTA: \n" +
                       "ID: ${it.id} \n" +
                       "Fecha de creación: ${convertirFecha(it.creationDate)} \n" +
                       "Saldo: ${it.money}"
               binding.btnCrearCuenta.visibility = View.GONE

           } ?: run {
               binding.txtMostrarTarjetas.text = "No tiene tarjetas asociadas"
           }
       }


        binding.btnCrearCuenta.setOnClickListener{
            userViewModel.usuarioLogueado.value?.let { user ->
                accountViewModel.createAccountForUser(user.id)
            }
        }

        binding.tarjetaMiInformacion.setOnClickListener {
            cambiarVisibilidadCard(binding.infoContainerMiInformacion)
        }
        binding.tarjetaMisTarjetas.setOnClickListener {
            cambiarVisibilidadCard(binding.infoContainerMiMistarjetas)
        }

        binding.imagenEditIcon.setOnClickListener {
           pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun cambiarVisibilidadCard(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri!=null){
            binding.imagenFotoPerfil.setImageURI(uri)
            userViewModel.localUser.value?.let { user ->
                userViewModel.updateUserProfileImage(user, uri.toString())
            }
        }else{
            Toast.makeText(requireContext(), "No selecciona ninguna imágen", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun convertirFecha(fecha: String): String{
        // Formatear la fecha y hora según el formato deseado
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatoEntrada.timeZone = TimeZone.getTimeZone("UTC") // Asegurarse de que el tiempo está en UTC
        val formatoSalida = SimpleDateFormat("MMM d, yyyy, hh:mm a", Locale.getDefault())
        val fecha = formatoEntrada.parse(fecha)
        return formatoSalida.format(fecha)
    }
}