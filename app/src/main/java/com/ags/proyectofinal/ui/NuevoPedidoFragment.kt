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
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentNuevoPedidoBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*private const val TIPO = "tipo"
private const val PEDIDO_ID = "pedido_id"*/
class NuevoPedidoFragment (
    private var pedido: PedidoEntity? = null
): Fragment() {

    private var _binding: FragmentNuevoPedidoBinding? = null
    private val binding get() = _binding!!


    private var producto: ProductoDto ?= null

    private lateinit var productoRepository: ProductoRepository
    private var itemSelected: Long = 0
    private var imageSelected: String = ""
    private var listaProductos: List<ProductoDto> = emptyList()
    private var listaNombresProductos: MutableList<String> = emptyList<String>().toMutableList()

    /*private var tipo: Char = '_'
    private var pedidoId: Long = -1
    private lateinit var repository: PedidoRepository*/


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = (requireContext().applicationContext as ProyectoFinalApp).repository
        arguments?.let {
            tipo = it.getChar(TIPO, '_')
            pedidoId = it.getLong(PEDIDO_ID, -1)
            if (tipo != 'N') {
                lifecycleScope.launch {
                    pedido = repository.getPedidoById(pedidoId)

                }
            }
        }

    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNuevoPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (pedido != null) {
            binding.tvTitulo.text = getString(R.string.actualizarPedido)
        } else {
            binding.tvTitulo.text = getString(R.string.nuevoPedido)
        }

        productoRepository = (requireContext().applicationContext as ProyectoFinalApp).productoRepository

        load()

        // konecta , paypal (pasarelas de pago)


        binding.apply {

            etProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    itemSelected = position.toLong() + 1
                    producto = listaProductos[position]
                    imageSelected = listaProductos[position].imageURL!!
                    Glide.with(requireContext())
                        .load(imageSelected)
                        .into(ivProduct)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }
            btNext.setOnClickListener {
                if (pedido == null ) {
                    parentFragmentManager.beginTransaction().replace(
                        R.id.fgContainerView,
                        PresentacionFragment.newInstance(
                            pedido = pedido,
                            producto = producto!!
                            /*tipo = tipo,
                            productoId = itemSelected,
                            productoImageURL = imageSelected,
                            pedidoId = -1*/
                        )
                    ).addToBackStack(null).commit()
                } else {
                    parentFragmentManager.beginTransaction().replace(
                        R.id.fgContainerView,
                        PresentacionFragment.newInstance(
                            pedido = pedido,
                            producto = producto!!
                            /*tipo = tipo,
                            productoId = itemSelected,
                            productoImageURL = imageSelected,
                            pedidoId = pedidoId*/
                        )
                    ).addToBackStack(null).commit()
                }

            }
        }
    }

    private fun load() {
        binding.tvProduct.visibility = View.GONE
        binding.etProduct.visibility = View.GONE
        binding.ivProduct.visibility = View.GONE
        binding.btNext.visibility = View.GONE
        binding.btReload.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
        lifecycleScope.launch {
            val call: Call<List<ProductoDto>> = productoRepository.getCatalogoProductosApiary()
            call.enqueue(object : Callback<List<ProductoDto>> {
                override fun onResponse(
                    call: Call<List<ProductoDto>>,
                    response: Response<List<ProductoDto>>
                ) {
                    listaNombresProductos.clear()
                    binding.tvProduct.visibility = View.VISIBLE
                    binding.etProduct.visibility = View.VISIBLE
                    binding.ivProduct.visibility = View.VISIBLE
                    binding.btNext.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    response.body()?.let { productos ->
                        listaProductos = productos
                        for (i in productos.indices) {
                            listaNombresProductos.add(listaProductos[i].name.toString())
                        }

                        binding.apply {
                            etProduct.adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.spinner_layout,
                                R.id.spinnerText,
                                listaNombresProductos
                            )
                            if (pedido != null) {
                                itemSelected = pedido!!.productoId
                                producto = listaProductos.find { it.id == pedido!!.productoId }
                                imageSelected = producto!!.imageURL!!
                                // Arreglar esto despues
                                // Con get by index (ya que tengo el producto busco a que indice está asociado)
                                etProduct.setSelection((itemSelected - 1).toInt())
                                Glide.with(requireContext())
                                    .load(imageSelected)
                                    .into(ivProduct)
                            }
                            else {
                                itemSelected = 0
                                producto = listaProductos.first()
                                imageSelected = producto!!.imageURL!!
                                etProduct.setSelection(itemSelected.toInt())
                                Glide.with(requireContext())
                                    .load(imageSelected)
                                    .into(ivProduct)
                            }
                        }

                    }
                }

                override fun onFailure(call: Call<List<ProductoDto>>, t: Throwable) {
                    //Manejo del error
                    binding.tvProduct.visibility = View.GONE
                    binding.etProduct.visibility = View.GONE
                    binding.ivProduct.visibility = View.GONE
                    binding.btNext.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.btReload.visibility = View.VISIBLE

                    binding.btReload.setOnClickListener {
                        load()
                    }

                }

            })

        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(pedido: PedidoEntity?) = NuevoPedidoFragment(pedido = pedido )/*.apply {
            arguments = Bundle().apply {
                putChar(TIPO,tipo)
                putLong(PEDIDO_ID,pedidoId)
            }
        }*/
    }

}