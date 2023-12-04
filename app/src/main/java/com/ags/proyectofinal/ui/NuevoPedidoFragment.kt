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


private const val TIPO = "tipo"
private const val PEDIDO_ID = "pedido_id"
class NuevoPedidoFragment (
): Fragment() {

    private var _binding: FragmentNuevoPedidoBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId = ""

    private lateinit var repository: PedidoRepository
    private lateinit var productoRepository: ProductoRepository
    private var pedido: PedidoEntity? = null
    private var itemSelected: Long = 0
    private var imageSelected: String = ""
    private var listaProductos: List<ProductoDto> = emptyList()
    private var listaNombresProductos: MutableList<String> = emptyList<String>().toMutableList()
    private var tipo: Char = '_'
    private var pedidoId: Long = -1


    override fun onCreate(savedInstanceState: Bundle?) {
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

    }

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
        if (tipo != 'N') {
            binding.tvTitulo.text = getString(R.string.actualizarPedido)
        } else {
            binding.tvTitulo.text = getString(R.string.nuevoPedido)
        }

        productoRepository = (requireContext().applicationContext as ProyectoFinalApp).productoRepository
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser
        if (user != null) {
            userId = user!!.uid
        }

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
                    imageSelected = listaProductos[position].imageURL!!
                    Glide.with(requireContext())
                        .load(imageSelected)
                        .into(ivProduct)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }
            btNext.setOnClickListener {
                if (tipo == 'N') {
                    parentFragmentManager.beginTransaction().replace(
                        R.id.fgContainerView,
                        PresentacionFragment.newInstance(
                            tipo = tipo,
                            productoId = itemSelected,
                            productoImageURL = imageSelected,
                            pedidoId = -1
                        )
                    ).addToBackStack(null).commit()
                } else {
                    parentFragmentManager.beginTransaction().replace(
                        R.id.fgContainerView,
                        PresentacionFragment.newInstance(
                            tipo = tipo,
                            productoId = itemSelected,
                            productoImageURL = imageSelected,
                            pedidoId = pedidoId
                        )
                    ).addToBackStack(null).commit()
                    Toast.makeText(requireContext(), "Frag${itemSelected}", Toast.LENGTH_SHORT)
                        .show()
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
                            if (tipo != 'N') {
                                itemSelected = pedido!!.productoId
                                imageSelected =
                                    listaProductos[(itemSelected - 1).toInt()].imageURL!!
                                etProduct.setSelection((itemSelected - 1).toInt())
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


    private fun validateFields(): Boolean {
        return true
    }

    private fun setErrorMessages() {
        binding.apply {
            if (itemSelected.toInt() == 0) {
                (etProduct.selectedView as TextView).error = getString(R.string.error)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(tipo: Char,pedidoId:Long) = NuevoPedidoFragment().apply {
            arguments = Bundle().apply {
                putChar(TIPO,tipo)
                putLong(PEDIDO_ID,pedidoId)
            }
        }
    }

}