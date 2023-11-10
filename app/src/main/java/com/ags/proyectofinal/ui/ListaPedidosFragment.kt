package com.ags.proyectofinal.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentListaPedidosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.io.IOException

class ListaPedidosFragment : Fragment() {

    private var _binding : FragmentListaPedidosBinding ? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId = ""

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

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser
        if (user != null){
            userId = user!!.uid
        }

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
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.eliminarPedido))
                    .setMessage(getString(R.string.mensajeEliminarPedido))
                    .setPositiveButton(getString(R.string.botonPositivo)){ _,_ ->
                        try{
                            lifecycleScope.launch {
                                repository.deletePedido(pedido)
                                updateUI()
                                Toast.makeText(requireContext(), getString(R.string.eliminacionExitosa), Toast.LENGTH_SHORT).show()

                            }
                        }catch (e: IOException){
                            e.printStackTrace()
                            Toast.makeText(requireContext(), getString(R.string.eliminacionFallida), Toast.LENGTH_SHORT).show()
                        }

                    }.setNegativeButton(getString(R.string.botonNegativo)){ dialog,_ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
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
            //Toast.makeText(requireContext(), "ID: $userId", Toast.LENGTH_SHORT).show()
            pedidos = repository.getAllPedidosByUser(userId)

            if (pedidos.isNotEmpty()) {

            }else{
                //Toast.makeText(requireContext(), getString(R.string.noRegistros), Toast.LENGTH_LONG).show()
            }
            pedidoAdapter.updateList(pedidos)
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance() = ListaPedidosFragment()

    }




}