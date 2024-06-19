package com.example.alkewalletm5.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.data.response.TransactionResponse
import com.squareup.picasso.Picasso

class TransactionAdapter(private val transactions: List<TransactionResponse>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreReceptor: TextView = itemView.findViewById(R.id.txtYaraMovimiento)
        val monto: TextView = itemView.findViewById(R.id.MontoEnvioYara)
        val fecha: TextView = itemView.findViewById(R.id.txtfechaYara)

        fun bind(transaction: TransactionResponse) {
            nombreReceptor.text = "ID: ${transaction.id}"
            monto.text = "$${transaction.amount}"
            fecha.text = transaction.date
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
}