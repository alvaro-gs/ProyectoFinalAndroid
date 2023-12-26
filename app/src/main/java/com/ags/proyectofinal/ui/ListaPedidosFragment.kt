package com.ags.proyectofinal.ui

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

        if (user?.uid == "AMufvA6zA4ZaAOWrnegGS5qyecI3"){
            binding.tvTitulo.text = getString(R.string.listaPedidosAdmin)
        }else{
            binding.tvTitulo.text = getString(R.string.listaPedidos)
        }

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
                .replace(R.id.fgContainerView, NuevoPedidoFragment.newInstance(pedido = null,/*tipo = 'N', pedidoId = -1*/))
                .addToBackStack("NuevoPedidoFragment")
                .commit()
        }
    }


    private fun onPedidoClicked(pedido:PedidoEntity,action:String){
        when (action){
            "Update" -> {
                /*parentFragmentManager.beginTransaction()
                    .replace(R.id.fgContainerView, EditarPedidoFragment.newInstance(pedido = pedido, updateUI = {updateUI()}))
                    .addToBackStack("EditarPedidoFragment")
                    .commit()*/
                if (user?.uid == "AMufvA6zA4ZaAOWrnegGS5qyecI3"){
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fgContainerView, EditarPedidoAdminFragment.newInstance(pedido = pedido))
                        .addToBackStack("EditarPedidoAdminFragment")
                        .commit()
                }
                else{
                    var status = pedido.status
                    if (status == 0.toShort()){
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fgContainerView, NuevoPedidoFragment.newInstance(pedido = pedido)/*,tipo = 'E', pedidoId = pedido.id)*/)
                            .addToBackStack("EditarPedidoFragment")
                            .commit()
                    } else {
                        var message = ""
                        if (status == 5.toShort()){
                            message = getString(R.string.noCancelarPedido)
                        }
                        else{
                            message = getString(R.string.noEditarPedido)
                        }

                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.editarPedido))
                            .setMessage(message)
                            .setPositiveButton(getString(R.string.botonPositivo)){ dialog,_ ->
                               dialog.dismiss()
                            }
                            .create()
                            .show()

                    }

                }
            }
            "Delete" -> {
                if (user?.uid == "AMufvA6zA4ZaAOWrnegGS5qyecI3"){

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
                else {
                    var status = pedido.status
                    if (status == 0.toShort()){
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.cancelarPedido))
                            .setMessage(getString(R.string.mensajeCancelarPedido))
                            .setPositiveButton(getString(R.string.botonPositivo)){ _,_ ->
                                try{
                                    lifecycleScope.launch {
                                        pedido.status = 5
                                        repository.updatePedido(pedido)
                                        updateUI()
                                        Toast.makeText(requireContext(), getString(R.string.cancelacionExitosa), Toast.LENGTH_SHORT).show()

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
                    else{
                        var message = ""
                        if (status == 5.toShort()){
                            message = getString(R.string.noCancelarPedido)
                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.cancelarPedido))
                                .setMessage(message)
                                .setPositiveButton(getString(R.string.botonPositivo)){ dialog,_ ->
                                    dialog.dismiss()
                                }
                                .create()
                                .show()

                        }
                        else{
                            message = getString(R.string.mensajeCancelarPedidoSinReembolso)
                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.cancelarPedido))
                                .setMessage(message)
                                .setPositiveButton(getString(R.string.botonPositivo)){ _,_ ->
                                    try{
                                        lifecycleScope.launch {
                                            pedido.status = 5
                                            repository.updatePedido(pedido)
                                            updateUI()
                                            Toast.makeText(requireContext(), getString(R.string.cancelacionExitosa), Toast.LENGTH_SHORT).show()

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
                    }
                }
            }

            "Detail" ->{
                if (user?.uid == "AMufvA6zA4ZaAOWrnegGS5qyecI3"){
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fgContainerView, EditarPedidoAdminFragment.newInstance(pedido = pedido))
                        .addToBackStack("DetallePedidoFragment")
                        .commit()
                }
                else{
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fgContainerView, DetallePedidoFragment.newInstance(pedido = pedido))
                        .addToBackStack("DetallePedidoFragment")
                        .commit()
                }
            }
        }
    }

    private fun updateUI(){
        lifecycleScope.launch {

            pedidos = if (user?.uid == "AMufvA6zA4ZaAOWrnegGS5qyecI3"){
                repository.getAllPedidos()
            }else{
                repository.getAllPedidosByUser(userId)
            }


            if (pedidos.isNotEmpty()) {

            }else{

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