package com.example.alkewalletm5.data.local

import com.example.alkewalletm5.data.model.Transaccion

class TransaccionesDataSet (){

    fun createTransactionsForUSer():MutableList<Transaccion>{
        return mutableListOf(
            Transaccion("id1", 1,2000.0, "UsuarioId1", "UsuarioId2", "19/05/2024"),
            Transaccion("id2", 1,3000.0, "UsuarioId1", "UsuarioId3","18/05/2024"),
            Transaccion("id2", 1,3000.0, "UsuarioId1", "UsuarioId3","17/05/2024"),
            Transaccion("id2", 1,3000.0, "UsuarioId1", "UsuarioId3","16/05/2024"),
            Transaccion("id2", 1, 3000.0, "UsuarioId1", "UsuarioId3","15/05/2024"),
        )

    }

    fun dataEmpty():MutableList<Transaccion>{
        return mutableListOf<Transaccion>()
    }
}