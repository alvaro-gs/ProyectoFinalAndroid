package com.ags.proyectofinal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentEditarPedidoAdminBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.io.IOException


class EditarPedidoAdminFragment(private var pedido : PedidoEntity) : Fragment() {

    private var _binding: FragmentEditarPedidoAdminBinding?= null
    private val binding get() = _binding!!

    private var listaEstatus : MutableList<String> = emptyList<String>().toMutableList()

    private var itemSelected : Short = 0

    private lateinit var repository: PedidoRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarPedidoAdminBinding.inflate(layoutInflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireContext().applicationContext as ProyectoFinalApp).repository
        listaEstatus = arrayListOf("Recibido","En proceso","Entregado","Enviado","Finalizado","Cancelado")
        var detail = ""
        var status = ""
        if(pedido.postalCode != -1){

            detail = getString(R.string.entregaEn) + " " + pedido.street +  getString(R.string.coma) + " " + pedido.suburb + getString(
                R.string.coma) + " " + getString(R.string.codigoPostal) + " " + pedido.postalCode.toString() + " " + getString(
                R.string.notasAdicionales) + " " + pedido.notes

        }
        else{
            detail = getString(R.string.entregaSucursal)
        }

        binding.apply {
            tvDetail.text = detail
            editStatus.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_layout,
                R.id.spinnerText,
                listaEstatus
            )
            itemSelected = pedido.status
            editStatus.setSelection(itemSelected.toInt())

            tvPay.text = pedido.remainingPayment.toString()
            Glide.with(requireContext())
                .load(pedido.imageURL)
                .into(ivProduct)


        }


        binding.btSave.setOnClickListener {
            pedido.status = itemSelected
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.guardarCambios))
                .setMessage(getString(R.string.guardarCambiosAlerta))
                .setPositiveButton(getString(R.string.botonPositivo)){ dialog,_ ->
                    try {
                        lifecycleScope.launch() {
                            repository.updatePedido(pedido)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fgContainerView, ListaPedidosFragment.newInstance())
                                .commit()
                        }

                    }catch (e:IOException){
                        Toast.makeText(requireContext(), getString(R.string.errorGenerico), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }.setNegativeButton(getString(R.string.botonNegativo)){ dialog,_ ->
                    dialog.dismiss()
                }.create().show()

        }

        binding.editStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemSelected = position.toShort()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(requireContext(), "Dest", Toast.LENGTH_SHORT).show()
        _binding = null
    }

    companion object {

        fun newInstance(pedido:PedidoEntity) = EditarPedidoAdminFragment(pedido)
    }
}