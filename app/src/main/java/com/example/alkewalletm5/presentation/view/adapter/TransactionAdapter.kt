package com.example.alkewalletm5.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Destinatario
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.data.response.TransactionResponse
import com.example.alkewalletm5.data.response.UserResponse
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TransactionAdapter(private val transactions: List<TransactionResponse>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreReceptor: TextView = itemView.findViewById(R.id.txtYaraMovimiento)
        val monto: TextView = itemView.findViewById(R.id.MontoEnvioYara)
        val fecha: TextView = itemView.findViewById(R.id.txtfechaYara)
        val iconImageView: ImageView = itemView.findViewById(R.id.IconoEnvioYara)

        fun bind(transaction: TransactionResponse) {
            // Aquí decides qué icono mostrar basado en el tipo de transacción
            if (transaction.type=="sendMoney") {
                iconImageView.setImageResource(R.drawable.send_icon_yellow)
                monto.text = "-$${transaction.amount}"
            } else {
                iconImageView.setImageResource(R.drawable.request_icon2)
                monto.text = "+$${transaction.amount}"
            }
            nombreReceptor.text = "ID: ${transaction.id}"
            fecha.text = cambiarFormatoFecha(transaction.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaccion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    private fun cambiarFormatoFecha(fechaOriginal: String): String {

        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatoEntrada.timeZone = TimeZone.getTimeZone("UTC") // Asegurarse de que el tiempo está en UTC
        val formatoSalida = SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault())
        val fecha = formatoEntrada.parse(fechaOriginal)
        return formatoSalida.format(fecha)
    }
}