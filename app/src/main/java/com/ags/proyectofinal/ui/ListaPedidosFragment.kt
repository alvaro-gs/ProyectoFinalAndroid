package com.ags.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentListaPedidosBinding
import kotlinx.coroutines.launch
import java.io.IOException

class ListaPedidosFragment : Fragment() {

    private var _binding : FragmentListaPedidosBinding ? = null
    private val binding get() = _binding!!

    private var pedidos : List<PedidoEntity> = emptyList()
    private lateinit var repository: PedidoRepository
    private lateinit var pedidoAdapter: PedidoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaPedidosBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        repository = (activity?.application as ProyectoFinalApp).repository

        pedidoAdapter = PedidoAdapter(){
            pedido,action -> onPedidoClicked(pedido, action)
        }

        binding.pedidoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pedidoAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),1))

        }

        updateUI()


        binding.btNew.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, NuevoPedidoFragment.newInstance(updateUI = {updateUI()}))
                .addToBackStack("NuevoPedidoFragment")
                .commit()
        }
    }


    private fun onPedidoClicked(pedido:PedidoEntity,action:String){
        when (action){
            "Update" -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fgContainerView, EditarPedidoFragment.newInstance(pedido = pedido, updateUI = {updateUI()}))
                    .addToBackStack("EditarPedidoFragment")
                    .commit()
            }
            "Delete" -> {
                try{
                    lifecycleScope.launch {
                        repository.deletePedido(pedido)
                        updateUI()

                    }
                }catch (e: IOException){
                    e.printStackTrace()
                }


            }

            "Detail" ->{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fgContainerView, DetallePedidoFragment.newInstance(pedido = pedido, updateUI = {updateUI()}))
                    .addToBackStack("DetallePedidoFragment")
                    .commit()
            }
        }
    }

    private fun updateUI(){
        lifecycleScope.launch {
            pedidos = repository.getAllPedidos()

            if (pedidos.isNotEmpty()) {

            }else{
                Toast.makeText(requireContext(), getString(R.string.noRegistros), Toast.LENGTH_LONG).show()
            }
            pedidoAdapter.updateList(pedidos)
        }
    }

    companion object {

        fun newInstance() = ListaPedidosFragment()

    }




}