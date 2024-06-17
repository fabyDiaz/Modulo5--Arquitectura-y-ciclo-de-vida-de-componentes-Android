package com.example.alkewalletm5.presentation.view.adapter
/**
 * Clase Adapter
 * @author Fabiola Díaz
 * @since v1.1 24/05/2024
 *
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Destinatario
import com.example.alkewalletm5.data.response.UserResponse
import com.squareup.picasso.Picasso

class DestinatarioAdpater (context: Context, private var items: List<UserResponse>) :
    ArrayAdapter<UserResponse>(context, 0, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.destinatarios_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.destinatarios_item)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val item = getItem(position)

        //val imageView = view.findViewById<ImageView>(R.id.imagenPerfilEnviarDinero)
        val nameTextView = view.findViewById<TextView>(R.id.textNombreEnviarDinero)
        val emailTextView = view.findViewById<TextView>(R.id.textEmailEnviarDinero)
    // Si item no es nulo, se ejecuta el bloque dentro de let
        item?.let {
            Picasso.get()
                .load(R.mipmap.pp3)
                .centerCrop()
                .fit()
                .into(view.findViewById<ImageView>(R.id.imagenPerfilEnviarDinero))
           // imageView.setImageResource(it.fotoPerfil)
            nameTextView.text = "${it.firstName} ${it.lastName}"
            emailTextView.text = it.email
        }

        return view
    }
    fun updateItems(newItems: List<UserResponse>) {
        items = newItems
        notifyDataSetChanged()
    }

}