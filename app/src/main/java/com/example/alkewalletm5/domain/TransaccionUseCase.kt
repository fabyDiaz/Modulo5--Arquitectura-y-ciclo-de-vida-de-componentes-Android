package com.example.alkewalletm5.domain

import com.example.alkewalletm5.data.local.TransaccionesDataSet
import com.example.alkewalletm5.data.model.Transaccion

class TransaccionUseCase {
        //inyección de dependencia
        val transactionDataSet = TransaccionesDataSet()

        fun getAllTransactionForUser(userId:String): MutableList<Transaccion> {
            return transactionDataSet.createTransactionsForUSer()
        }
}