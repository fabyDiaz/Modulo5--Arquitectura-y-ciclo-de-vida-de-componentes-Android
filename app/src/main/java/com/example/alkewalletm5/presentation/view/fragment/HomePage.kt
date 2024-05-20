package com.example.alkewalletm5.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewalletm5.R
import com.example.alkewalletm5.data.local.TransaccionesDataSet
import com.example.alkewalletm5.data.model.Transaccion
import com.example.alkewalletm5.databinding.FragmentHomePageBinding
import com.example.alkewalletm5.presentation.view.adapter.TransaccionAdapter
import com.example.alkewalletm5.presentation.viewmodel.TransaccionViewModel


class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransaccionAdapter
    private lateinit var itemList: MutableList<Transaccion>
    private val transaccionViewModel: TransaccionViewModel by activityViewModels()
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
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(view)
        val btnInresarDinero = view.findViewById<Button>(R.id.btnIngresarDineroHome)
        val btnEnviarDinero = view.findViewById<Button>(R.id.btnEnviarDineroHome)
        val btnPerfil = view.findViewById<ImageView>(R.id.imagenHomeAmanda)

        btnInresarDinero.setOnClickListener { v: View? -> navController.navigate(R.id.requestMoney) }
        btnEnviarDinero.setOnClickListener { v: View? -> navController.navigate(R.id.sendMoney) }
        btnPerfil.setOnClickListener { v: View? -> navController.navigate(R.id.profilePage) }


        binding.recyclerTransacciones.layoutManager = LinearLayoutManager(context)
        adapter = TransaccionAdapter()
        binding.recyclerTransacciones.adapter = adapter

        // Initialize the data list
       //itemList = TransaccionesDataSet().dataEmpty()

       /* itemList = TransaccionesDataSet().createTransactionsForUSer()

        // Initialize the adapter with data
        initAdapter()*/

        transaccionViewModel.transacciones.observe(viewLifecycleOwner) { transacciones ->
            adapter.items = transacciones
            adapter.notifyDataSetChanged()
            updateEmptyState()
        }

        updateEmptyState()


    }

   /*fun initAdapter(){
       adapter.items = itemList
       adapter.notifyDataSetChanged()

       if (adapter.items.isEmpty()) {
           binding.layoutTransaccionesEmpty.visibility = View.VISIBLE
           binding.recyclerTransacciones.visibility = View.GONE
       } else {
           binding.layoutTransaccionesEmpty.visibility = View.GONE
           binding.recyclerTransacciones.visibility = View.VISIBLE
       }
   }*/

    private fun updateEmptyState() {
        if (adapter.items.isEmpty()) {
            binding.layoutTransaccionesEmpty.visibility = View.VISIBLE
            binding.recyclerTransacciones.visibility = View.GONE
        } else {
            binding.layoutTransaccionesEmpty.visibility = View.GONE
            binding.recyclerTransacciones.visibility = View.VISIBLE
        }
    }
}