package com.example.alkewalletm5.presentation.view.fragment
/**
 * Clase Fragmento
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.alkewalletm5.R
import com.example.alkewalletm5.databinding.FragmentProfilePageBinding
import com.example.alkewalletm5.presentation.viewmodel.UsuarioViewModel
import com.squareup.picasso.Picasso

/**
 * Fragmento que representa la página de perfil del usuario.
 */
class ProfilePage : Fragment() {
    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private val usuarioViewModel: UsuarioViewModel by activityViewModels()

   // private val REQUEST_CODE_PERMISSION = 1001
    //private val REQUEST_CODE_IMAGE_PICK = 1002

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
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        //Navegación con el botón de retroceder del teléfono hacia el homePage
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homePage)
            }
        })
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

        //Actualiza la foto y nombre del perfil del  usuario logueado
        usuarioViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            binding.textNombrePerfil.text = usuario.nombre
            usuario.imgPerfil?.let { uri ->
                Picasso.get()
                    .load(uri)
                    .centerCrop()
                    .fit()
                    .into(binding.imagenFotoPerfil)
            }
           // binding.imagenFotoPerfil.setImageResource(usuario.imgPerfil)
        }

        //Actualiza los cardView del usuario logueado. Por el momento solo muestra
        //Mi información y mis trarjetas
        usuarioViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            binding.txtMostrarInformaccion.text = "Nombre: ${usuario.nombre} \n" +
                                                    "Apellido: ${usuario.apellido} \n" +
                                                        "Email: ${usuario.email} \n"
            binding.txtMostrarTarjetas.text = "No tiene tarjetas asociadas"
        }

        //Muestra la información cuando se hace click en el CardView "Mi información"
        binding.tarjetaMiInformacion.setOnClickListener{
            cambiarVisibilidadCard(binding.infoContainerMiInformacion)
        }
        //Muestra la información cuando se hace click en el CardView "Mis tarjetas"
        binding.tarjetaMisTarjetas.setOnClickListener{
            cambiarVisibilidadCard(binding.infoContainerMiMistarjetas)
        }

    /*    binding.imagenEditIcon.setOnClickListener {
            requestStoragePermission()
        }*/


    }

    /**
     * Cambia la visibilidad del layout que se encuentran bajo los CarsView, para cuando le hagan
     * Click en el CardView se muestre o se oculte
     */
    private fun cambiarVisibilidadCard(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

  /*  private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    private fun saveImageUri(uri: Uri) {
        usuarioViewModel.actualizarImagenUsuarioLogueado(uri)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                Picasso.get()
                    .load(it)
                    .centerCrop()
                    .fit()
                    .into(binding.imagenFotoPerfil)
                saveImageUri(it)
            }
        }
    }*/

    /**
     * Método llamado cuando la vista asociada con el fragmento está siendo destruida.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }

}