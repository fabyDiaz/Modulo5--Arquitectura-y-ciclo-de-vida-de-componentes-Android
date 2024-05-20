package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.TransaccionesDataSet
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.databinding.FragmentHomePageBinding
import com.example.alkewalletm5.databinding.FragmentSendMoneyBinding
import com.example.alkewalletm5.presentation.view.adapter.TransaccionAdapter

class HomePageEmpty : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentHomePageBinding
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


        initAdapter()

    }

    fun initAdapter(){
        val transaccionAdapter = TransaccionAdapter()
        binding.recyclerTransacciones.adapter = TransaccionAdapter()
        //sismoAdapter.sismos = Sismo.dataSismos
        transaccionAdapter.items = TransaccionesDataSet().dataEmpty()
        if(transaccionAdapter.items.isEmpty())
            binding.layoutTransaccionesEmpty.visibility = View.VISIBLE
        else
            binding.layoutTransaccionesEmpty.visibility = View.GONE
    }



}