package com.example.alkewalletm5.data.local

import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.model.Transaccion

class TransaccionesDataSet (){

    fun createTransactionsForUSer():MutableList<Transaccion>{
        return mutableListOf(
            Transaccion(id = "1", fotoPerfil = R.mipmap.pp1, monto = 100.0, idReceriver = "Receptor 1"),
            Transaccion(id = "2", fotoPerfil = R.mipmap.pp2, monto = 200.0, idReceriver = "Receptor 2"),
            Transaccion(id = "3", fotoPerfil = R.mipmap.pp3, monto = 300.0, idReceriver = "Receptor 3")
        )

    }

    fun dataEmpty():MutableList<Transaccion>{
        return mutableListOf<Transaccion>()
    }
}