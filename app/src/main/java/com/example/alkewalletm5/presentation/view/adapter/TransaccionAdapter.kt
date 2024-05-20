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

    var items = mutableListOf<Transaccion>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imagenYara)
        val nombreReceptor: TextView = view.findViewById(R.id.txtYaraMovimiento)
        val monto: TextView = view.findViewById(R.id.MontoEnvioYara)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaccion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imagen.setImageResource(item.fotoPerfil)
        holder.nombreReceptor.text = item.idReceriver
        holder.monto.text = item.monto.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}