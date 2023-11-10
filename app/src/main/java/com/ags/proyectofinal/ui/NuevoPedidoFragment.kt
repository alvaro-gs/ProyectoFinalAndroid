package com.ags.proyectofinal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentNuevoPedidoBinding
import com.ags.proyectofinal.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList


class NuevoPedidoFragment (
    private val updateUI: () -> Unit
): Fragment() {

    private var _binding: FragmentNuevoPedidoBinding ? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId = ""
    private lateinit var repository: PedidoRepository
    private lateinit var productoRepository: ProductoRepository
    private var itemSelected : Long = 0
    private var listaProductos: List<ProductoDto> = emptyList()
    private var listaNombresProductos: MutableList<String> = emptyList<String>().toMutableList()
    private var listaProductosAux: MutableList<Int> = emptyList<Int>().toMutableList()

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
        productoRepository = (requireContext().applicationContext as ProyectoFinalApp).productoRepository
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser
        if (user!= null){
            userId = user!!.uid
        }


        lifecycleScope.launch {
            val call: Call<List<ProductoDto>> = productoRepository.getCatalogoProductosApiary()

            call.enqueue(object: Callback<List<ProductoDto>> {
                override fun onResponse(
                    call: Call<List<ProductoDto>>,
                    response: Response<List<ProductoDto>>
                ) {
                    //binding.pbLoading.visibility = View.GONE
                    Log.d(Constants.LOGTAG, "Respuesta del servidor ${response.body()}")
                    response.body()?.let {productos ->
                        listaProductos = productos
                        listaNombresProductos.add("Ninguno Seleccionado")
                        for (i in productos.indices){
                            listaNombresProductos.add(listaProductos[i].name.toString())
                        }

                    }
                }

                override fun onFailure(call: Call<List<ProductoDto>>, t: Throwable) {
                    //Manejo del error
                   ///binding.pbLoading.visibility = View.GONE
                    Log.d(Constants.LOGTAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(), getString(R.string.errorConexion), Toast.LENGTH_LONG).show()
                }

            })

        }

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

            listaProductosAux.add(0)
            listaProductosAux.add(1)
            listaProductosAux.add(2)
            listaProductosAux.add(3)
            listaProductosAux.add(4)
            listaProductosAux.add(5)
            listaProductosAux.add(6)
            listaProductosAux.add(7)
            listaProductosAux.add(8)
            //var adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,listaNombresProductos)
            var adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,listaProductosAux)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            etProduct.adapter = adapter

            // konecta , paypal (pasarelas de pago)

            etProduct.setSelection(0)
            etProduct.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //itemSelected = listaNombresProductos[position].toLong()
                    itemSelected = listaProductosAux[position].toLong()
                    Toast.makeText(requireContext(), itemSelected.toString(), Toast.LENGTH_SHORT).show()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }
            btSend.setOnClickListener {
                if (validateFields()){
                    var name = getString(R.string.pedidoTexto) + itemSelected.toString()

                    var status: Short = 0
                    var street = ""
                    var suburb = ""
                    var postalCodeString = ""
                    var postalCode = -1
                    var notes = ""
                    var remainingPayment = 100.00
                    var imageURL = listaProductos[itemSelected.toInt()].imageURL

                    if(rgDelivery.checkedRadioButtonId == R.id.rbDomicilio){

                        street = etStreet.text.toString()
                        suburb = etSuburb.text.toString()
                        postalCodeString = etPostalCode.text.toString()
                        postalCode = postalCodeString.toInt()
                        notes = etNotes.text.toString()

                    }


                    var pedido = PedidoEntity(productoId = itemSelected, userId = userId, name = name, imageURL = imageURL!!, status = status, street = street, suburb = suburb, postalCode = postalCode , notes = notes, presentation = 1, remainingPayment = remainingPayment)

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