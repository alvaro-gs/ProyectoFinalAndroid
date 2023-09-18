package com.ags.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentEditarPedidoBinding
import kotlinx.coroutines.launch
import java.io.IOException


class EditarPedidoFragment(
    private var pedido : PedidoEntity,
    private val updateUI: () -> Unit

) : Fragment() {

    private var _binding : FragmentEditarPedidoBinding ? = null
    private val binding get() = _binding!!
    private lateinit var repository: PedidoRepository
    private var itemSelected : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarPedidoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as ProyectoFinalApp).repository


        binding.apply {
            if (pedido.postalCode != -1) {
                rbDomicilio.isChecked = true
                etStreet.setText(pedido.street)
                etSuburb.setText(pedido.suburb)
                etPostalCode.setText(pedido.postalCode.toString())
                etNotes.setText(pedido.notes)
            } else {
                rbSucursal.isChecked = true
                tvAdress.visibility = View.GONE
                tvStreet.visibility = View.GONE
                etStreet.visibility = View.GONE
                tvSuburb.visibility = View.GONE
                etSuburb.visibility = View.GONE
                tvPostalCode.visibility = View.GONE
                etPostalCode.visibility = View.GONE
                tvNotes.visibility = View.GONE
                etNotes.visibility = View.GONE

            }

            rgDelivery.setOnCheckedChangeListener { _, checkedId ->
                rbDomicilio.error = null
                rbSucursal.error = null
                etStreet.error = null
                etSuburb.error = null
                etPostalCode.error = null
                when (checkedId) {
                    R.id.rbSucursal -> {
                        tvAdress.visibility = View.GONE
                        tvStreet.visibility = View.GONE
                        etStreet.visibility = View.GONE
                        tvSuburb.visibility = View.GONE
                        etSuburb.visibility = View.GONE
                        tvPostalCode.visibility = View.GONE
                        etPostalCode.visibility = View.GONE
                        tvNotes.visibility = View.GONE
                        etNotes.visibility = View.GONE

                    }

                    R.id.rbDomicilio -> {
                        binding.tvAdress.visibility = View.VISIBLE
                        binding.tvStreet.visibility = View.VISIBLE
                        binding.etStreet.visibility = View.VISIBLE
                        binding.tvSuburb.visibility = View.VISIBLE
                        binding.etSuburb.visibility = View.VISIBLE
                        binding.tvPostalCode.visibility = View.VISIBLE
                        binding.etPostalCode.visibility = View.VISIBLE
                        binding.tvNotes.visibility = View.VISIBLE
                        binding.etNotes.visibility = View.VISIBLE
                        if (pedido.postalCode != -1) {
                            binding.rbDomicilio.isChecked = true
                            binding.etStreet.setText(pedido.street)
                            binding.etSuburb.setText(pedido.suburb)
                            binding.etPostalCode.setText(pedido.postalCode.toString())
                            binding.etNotes.setText(pedido.notes)
                        }
                    }

                }

            }


            var listaProductos = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8)
            var adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaProductos)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            etProduct.adapter = adapter

            itemSelected = pedido.productoId
            etProduct.setSelection((itemSelected - 1).toInt())
            etProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    itemSelected = listaProductos[position].toLong()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }
            btSend.setOnClickListener {
                if (validateFields()) {
                    var nameAct = getString(R.string.pedidoTexto) + itemSelected.toString()
                    var streetAct = ""
                    var suburbAct = ""
                    var postalCodeString = ""
                    var postalCodeAct = -1
                    var notesAct = etNotes.text.toString()
                    var remainingPaymentAct = 100.00
                    var imageURLAct = ""


                    if (rgDelivery.checkedRadioButtonId == R.id.rbDomicilio) {

                        streetAct = etStreet.text.toString()
                        suburbAct = etSuburb.text.toString()
                        postalCodeString = etPostalCode.text.toString()
                        postalCodeAct = postalCodeString.toInt()

                    }
                    // Actualizar imagen
                    when(itemSelected){
                        1.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/12N4v33IO_ZiG73D6GLRvPwOJRH_98BHl/view?usp=sharing"
                        }
                        2.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1PtRXOXiDNfc0G_ox4wSyiAnQzZhJrJyV/view?usp=sharing"
                        }
                        3.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1kR2bHPPFJifRfXuFh04x8X_69PFLMoWL/view?usp=sharing"
                        }
                        4.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1TJuli4AXqQsTe15GHHbczVkXXKGSpjU7/view?usp=sharing"
                        }
                        5.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1XPdYdp6bPF4Rz9MiwnSOUijmjOak7txB/view?usp=sharing"
                        }
                        6.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1PXnOIi-VeUZJHjOrgqtK-bAsWJYFHkSy/view?usp=sharing"
                        }
                        7.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1LIktDefaReuTg0SwtV_gf31tXuPw8q7Y/view?usp=sharing"
                        }
                        8.toLong()->{
                            imageURLAct = "https://drive.google.com/file/d/1CqY0USis4gL_dOqIUoh6YGXcuuIHT4tE/view?usp=sharing"
                        }
                    }
                    pedido.apply {
                        productoId = itemSelected
                        name = nameAct
                        street = streetAct
                        suburb = suburbAct
                        postalCode = postalCodeAct
                        notes = notesAct
                        imageURL = imageURLAct
                        remainingPayment = remainingPaymentAct

                    }

                    try {
                        lifecycleScope.launch {
                            repository.updatePedido(pedido)
                            updateUI
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fgContainerView, ListaPedidosFragment.newInstance())
                        .commit()

                } else {
                    setErrorMessages()

                }
            }
        }
    }
    private fun validateFields() : Boolean {
         binding.apply {
             if (rgDelivery.checkedRadioButtonId == R.id.rbDomicilio){
                 return (itemSelected != 0.toLong()
                         && etStreet.text.isNotEmpty()
                         && etSuburb.text.isNotEmpty()
                         && etPostalCode.text.isNotEmpty())
             }
             else {
                 return true
             }
         }
    }
    private fun setErrorMessages(){
        binding.apply {
            if(rgDelivery.checkedRadioButtonId == -1){
                rbDomicilio.error = getString(R.string.errorRadioButton)
                rbSucursal.error = getString(R.string.errorRadioButton)
            }
            if (rgDelivery.checkedRadioButtonId == R.id.rbDomicilio) {
                if (etStreet.text.isEmpty()) {
                    etStreet.error = getString(R.string.error)
                }
                if (etSuburb.text.isEmpty()) {
                    etSuburb.error = getString(R.string.error)
                }
                if (etPostalCode.text.isEmpty()) {
                    etPostalCode.error = getString(R.string.error)
                }
            }
            Toast.makeText(requireContext(),getString(R.string.errorMessage) , Toast.LENGTH_LONG).show()
        }
    }
    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }
    companion object {

        fun newInstance(pedido: PedidoEntity,updateUI: () -> Unit) = EditarPedidoFragment(pedido,updateUI)
    }
}