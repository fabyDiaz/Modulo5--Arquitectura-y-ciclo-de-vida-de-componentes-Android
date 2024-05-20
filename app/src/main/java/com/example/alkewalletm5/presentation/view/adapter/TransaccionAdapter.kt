package com.example.alkewalletm5.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Transaccion

class TransaccionAdapter(): RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {

    var items= mutableListOf<Transaccion>()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen = view.findViewById<ImageView>(R.id.imagenPerfilEnviarDinero)
        val nombreReceptor = view.findViewById<TextView>(R.id.textNombreEnviarDinero)
        val monto = view.findViewById<TextView>(R.id.editTextMontoEnviarDinero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaccion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imagen.setImageResource(item.fotoPerfil)
        holder.nombreReceptor.text = item.idReceriver
        holder.monto.text = item.monto.toString()
    }

    override fun getItemCount() = items.size
}