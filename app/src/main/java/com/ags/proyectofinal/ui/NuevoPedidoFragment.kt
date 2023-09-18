package com.ags.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentNuevoPedidoBinding
import kotlinx.coroutines.launch
import java.io.IOException


class NuevoPedidoFragment (
    private val updateUI: () -> Unit
): Fragment() {

    private var _binding: FragmentNuevoPedidoBinding ? = null
    private val binding get() = _binding!!

    private lateinit var repository: PedidoRepository
    private var itemSelected : Long = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNuevoPedidoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireContext().applicationContext as ProyectoFinalApp).repository

        binding.apply {

            tvAdress.visibility = View.GONE
            tvStreet.visibility = View.GONE
            etStreet.visibility = View.GONE
            tvSuburb.visibility = View.GONE
            etSuburb.visibility = View.GONE
            tvPostalCode.visibility = View.GONE
            etPostalCode.visibility = View.GONE
            tvNotes.visibility = View.GONE
            etNotes.visibility = View.GONE

            rgDelivery.setOnCheckedChangeListener { _, checkedId ->
                rbDomicilio.error = null
                rbSucursal.error = null
                etStreet.error = null
                etSuburb.error = null
                etPostalCode.error = null
                when(checkedId){
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
                        tvAdress.visibility = View.VISIBLE
                        tvStreet.visibility = View.VISIBLE
                        etStreet.visibility = View.VISIBLE
                        tvSuburb.visibility = View.VISIBLE
                        etSuburb.visibility = View.VISIBLE
                        tvPostalCode.visibility = View.VISIBLE
                        etPostalCode.visibility = View.VISIBLE
                        tvNotes.visibility = View.VISIBLE
                        etNotes.visibility = View.VISIBLE
                    }

                }

            }

            var listaProductos = arrayListOf(0,1,2,3,4,5,6,7,8)
            var adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,listaProductos)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            etProduct.adapter = adapter

            etProduct.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
                if (validateFields()){
                    var name = getString(R.string.pedidoTexto) + itemSelected.toString()
                    var userId : Long = 0
                    var status: Short = 0
                    var street = ""
                    var suburb = ""
                    var postalCodeString = ""
                    var postalCode = -1
                    var notes = ""
                    var remainingPayment = 100.00
                    var imageURL = ""

                    if(rgDelivery.checkedRadioButtonId == R.id.rbDomicilio){

                        street = etStreet.text.toString()
                        suburb = etSuburb.text.toString()
                        postalCodeString = etPostalCode.text.toString()
                        postalCode = postalCodeString.toInt()
                        notes = etNotes.text.toString()

                    }
                    // Actualizar imagen

                    when(itemSelected){
                        1.toLong()->{
                            imageURL = "https://drive.google.com/file/d/12N4v33IO_ZiG73D6GLRvPwOJRH_98BHl/view?usp=sharing"
                        }
                        2.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1PtRXOXiDNfc0G_ox4wSyiAnQzZhJrJyV/view?usp=sharing"
                        }
                        3.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1kR2bHPPFJifRfXuFh04x8X_69PFLMoWL/view?usp=sharing"
                        }
                        4.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1TJuli4AXqQsTe15GHHbczVkXXKGSpjU7/view?usp=sharing"
                        }
                        5.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1XPdYdp6bPF4Rz9MiwnSOUijmjOak7txB/view?usp=sharing"
                        }
                        6.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1PXnOIi-VeUZJHjOrgqtK-bAsWJYFHkSy/view?usp=sharing"
                        }
                        7.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1LIktDefaReuTg0SwtV_gf31tXuPw8q7Y/view?usp=sharing"
                        }
                        8.toLong()->{
                            imageURL = "https://drive.google.com/file/d/1CqY0USis4gL_dOqIUoh6YGXcuuIHT4tE/view?usp=sharing"
                        }
                    }

                    var pedido = PedidoEntity(productoId = itemSelected, userId = userId, name = name, imageURL = imageURL, status = status, street = street, suburb = suburb, postalCode = postalCode , notes = notes, presentation = 1, remainingPayment = remainingPayment)

                    try{
                        lifecycleScope.launch {
                            repository.insertPedido(pedido)
                            updateUI
                        }
                    }catch (e: IOException){
                        e.printStackTrace()
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fgContainerView, ListaPedidosFragment.newInstance())
                        .commit()

                }
                else{
                    setErrorMessages()

                }
            }
        }
    }
    private fun validateFields() : Boolean {
        binding.apply {
            when (rgDelivery.checkedRadioButtonId) {
                R.id.rbDomicilio -> {
                    return (itemSelected != 0.toLong()
                            && etStreet.text.isNotEmpty()
                            && etSuburb.text.isNotEmpty()
                            && etPostalCode.text.isNotEmpty())
                }

                R.id.rbSucursal -> {
                    return (itemSelected != 0.toLong())
                }
            }
            return false
        }
    }
    private fun setErrorMessages(){
        binding.apply {
            if(itemSelected == 0.toLong()){
                (etProduct.selectedView as TextView).error = getString(R.string.error)
            }
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
        fun newInstance(updateUI: () -> Unit) = NuevoPedidoFragment(updateUI)
    }

}