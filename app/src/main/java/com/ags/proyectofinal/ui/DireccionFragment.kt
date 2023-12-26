package com.ags.proyectofinal.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentDireccionBinding
import com.ags.proyectofinal.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.IOException

/*private const val PRODUCTO_ID = "producto_id"
private const val PRODUCTO_IMAGE_URL = "producto_image_url"
private const val PEDIDO_ID = "pedido_id"
private const val TIPO = "tipo"*/
private const val PRESENTACION = "presentacion_id"
private const val TOTAL_PAGAR = "total_pagar"

class DireccionFragment(
    private var pedido : PedidoEntity?= null,
    private var producto: ProductoDto
) : Fragment() {

    private var _binding: FragmentDireccionBinding?= null
    private val binding get() = _binding!!



    /*private var pedidoId: Long = -1

    private var tipo: Char = '_'

    private var productoId: Long = -1

    private var productoImageURL: String = ""*/

    private var presentacion: Int = -1

    private var totalPagar : Double = -1.0

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId = ""

    private lateinit var repository: PedidoRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = (requireContext().applicationContext as ProyectoFinalApp).repository
        arguments?.let {
            //tipo = it.getChar(TIPO,'_')
            //pedidoId = it.getLong(PEDIDO_ID,-1)
            //productoId = it.getLong(PRODUCTO_ID,-1)
            //productoImageURL = it.getString(PRODUCTO_IMAGE_URL,"")
            presentacion = it.getInt(PRESENTACION,-1)
            totalPagar = it.getDouble(TOTAL_PAGAR,0.0)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDireccionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            pbLoading.visibility = View.GONE
            if (pedido != null) {
                if (pedido!!.postalCode == -1) {
                    rbSucursal.isChecked = true
                    tvAdress.visibility = View.GONE
                    etStreet.visibility = View.GONE
                    etSuburb.visibility = View.GONE
                    etPostalCode.visibility = View.GONE
                    etNotes.visibility = View.GONE

                } else {
                    rbDomicilio.isChecked = true
                    etStreet.setText(pedido!!.street)
                    etSuburb.setText(pedido!!.suburb)
                    etPostalCode.setText(pedido!!.postalCode.toString())
                    etNotes.setText(pedido!!.notes)
                }

            } else {
                tvAdress.visibility = View.GONE
                etStreet.visibility = View.GONE
                etSuburb.visibility = View.GONE
                etPostalCode.visibility = View.GONE
                etNotes.visibility = View.GONE
                }
            }




        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser
        if (user != null){
            userId = user!!.uid
        }


        binding.apply {
            rgDelivery.setOnCheckedChangeListener { _, checkedId ->
                rbDomicilio.error = null
                rbSucursal.error = null
                etStreet.error = null
                etSuburb.error = null
                etPostalCode.error = null
                when (checkedId) {
                    R.id.rbSucursal -> {
                        tvAdress.visibility = View.GONE

                        etStreet.visibility = View.GONE

                        etSuburb.visibility = View.GONE

                        etPostalCode.visibility = View.GONE

                        etNotes.visibility = View.GONE
                    }

                    R.id.rbDomicilio -> {
                        tvAdress.visibility = View.VISIBLE

                        etStreet.visibility = View.VISIBLE

                        etSuburb.visibility = View.VISIBLE

                        etPostalCode.visibility = View.VISIBLE

                        etNotes.visibility = View.VISIBLE
                        if (pedido != null) {
                            if (pedido!!.postalCode != -1) {
                                binding.rbDomicilio.isChecked = true
                                binding.etStreet.setText(pedido!!.street)
                                binding.etSuburb.setText(pedido!!.suburb)
                                binding.etPostalCode.setText(pedido!!.postalCode.toString())
                                binding.etNotes.setText(pedido!!.notes)
                            }
                        }
                    }

                }

            }

            btFinish.setOnClickListener {
                if (validateFields()){
                    var name = producto.name
                    var productoId = producto.id
                    var imageURL = producto.imageURL
                    var status: Short = 0
                    var street = ""
                    var suburb = ""
                    var postalCodeString = ""
                    var postalCode = -1
                    var notes = ""
                    var remainingPayment = totalPagar / 2


                    if(rgDelivery.checkedRadioButtonId == R.id.rbDomicilio){

                        street = etStreet.text.toString()
                        suburb = etSuburb.text.toString()
                        postalCodeString = etPostalCode.text.toString()
                        postalCode = postalCodeString.toInt()
                        notes = etNotes.text.toString()

                    }

                    try{
                        lifecycleScope.launch {
                            if (pedido == null) {
                                var pedidoNuevo = PedidoEntity(productoId = productoId!!, userId = userId, name = name!!, imageURL = imageURL!!, status = status, street = street, suburb = suburb, postalCode = postalCode , notes = notes, presentation = presentacion, remainingPayment = remainingPayment)
                                repository.insertPedido(pedidoNuevo)
                            }
                            else{
                                pedido!!.apply {
                                    this.productoId = productoId!!
                                    this.name = name!!
                                    this.imageURL = imageURL!!
                                    this.status = status
                                    this.street = street
                                    this.suburb = suburb
                                    this.postalCode = postalCode
                                    this.notes = notes
                                    this.presentation = presentation
                                    this.remainingPayment = remainingPayment
                                }
                                repository.updatePedido(this@DireccionFragment.pedido!!)
                            }
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

    private fun validateFields() :Boolean{

        binding.apply {
            var street = etStreet.text.toString()
            var suburb = etSuburb.text.toString()
            var postalCode = etPostalCode.text.toString()
            when (rgDelivery.checkedRadioButtonId) {
                R.id.rbDomicilio -> {
                    return (street.isNotEmpty()
                            && suburb.isNotEmpty()
                            && postalCode.isNotEmpty())
                }

                R.id.rbSucursal -> {
                    return true
                }
            }
            return false
        }
    }

    private fun setErrorMessages(){
        binding.apply {
            var street = etStreet.text.toString()
            var suburb = etSuburb.text.toString()
            var postalCode = etPostalCode.text.toString()
            if(rgDelivery.checkedRadioButtonId == -1){
                rbDomicilio.error = getString(R.string.errorRadioButton)
                rbSucursal.error = getString(R.string.errorRadioButton)
            }
            if (rgDelivery.checkedRadioButtonId == R.id.rbDomicilio) {

                if (street.isEmpty()) {
                    etStreet.error = getString(R.string.error)
                }
                if (suburb.isEmpty()) {
                    etSuburb.error = getString(R.string.error)
                }
                if (postalCode.isEmpty()) {
                    etPostalCode.error = getString(R.string.error)
                }
            }
            Toast.makeText(requireContext(),getString(R.string.errorMessage) , Toast.LENGTH_LONG).show()
        }
    }


    companion object {

        fun newInstance(pedido: PedidoEntity?,producto: ProductoDto,presentacion:Int,totalPagar: Double) =
            DireccionFragment(pedido = pedido, producto = producto).apply {
                arguments = Bundle().apply {
                    //putChar(TIPO,tipo)
                    //putLong(PRODUCTO_ID, productoId)
                    //putString(PRODUCTO_IMAGE_URL,productoImageURL)
                    putInt(PRESENTACION,presentacion)
                    putDouble(TOTAL_PAGAR,totalPagar)
                    //putLong(PEDIDO_ID,pedidoId)

                }
            }
    }
}