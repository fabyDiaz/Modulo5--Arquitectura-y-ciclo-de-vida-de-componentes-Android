package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alkewalletm5.R

class HomePageEmpty : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page_empty, container, false)

       // itemList = TransaccionesDataSet().createTransactionsForUSer()



       // initAdapter()

    }

   /* fun initAdapter(){
        val transaccionAdapter = TransaccionAdapter()
        binding.recyclerTransacciones.adapter = TransaccionAdapter()
        //sismoAdapter.sismos = Sismo.dataSismos
        transaccionAdapter.items = TransaccionesDataSet().dataEmpty()
        if(transaccionAdapter.items.isEmpty())
            binding.layoutTransaccionesEmpty.visibility = View.VISIBLE
        else
            binding.layoutTransaccionesEmpty.visibility = View.GONE
    }*/



}