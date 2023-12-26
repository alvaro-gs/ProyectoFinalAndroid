package com.ags.proyectofinal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.DetalleProductoDto
import com.ags.proyectofinal.data.remote.model.Presentations
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentPresentacionBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PRODUCTO_ID = "producto_id"
private const val PRODUCTO_IMAGE_URL = "producto_image_url"
private const val PEDIDO_ID = "pedido_id"
private const val TIPO = "tipo"
class PresentacionFragment (
    private var producto: ProductoDto,
    private var pedido: PedidoEntity?
): Fragment() {


    private var listaPresentaciones: MutableList<Presentations> = emptyList<Presentations>().toMutableList()
    private var listaPresentacionesNombres: MutableList<String> = emptyList<String>().toMutableList()
    private var _binding: FragmentPresentacionBinding?= null
    private val binding get() = _binding!!

    /*
    private var productoId: Long = -1

    private var productoImageURL: String = ""

    private var pedidoId: Long = -1

    private var tipo: Char = '_'*/

    private var itemSelected : Int = 0

    private var totalPagar : Double = 0.0

    private lateinit var productoRepository: ProductoRepository

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            productoId = it.getLong(PRODUCTO_ID,-1)
            productoImageURL = it.getString(PRODUCTO_IMAGE_URL,"")
            tipo = it.getChar(TIPO,'_')
            pedidoId = it.getLong(PEDIDO_ID,-1)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPresentacionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productoRepository = (requireActivity().application as ProyectoFinalApp).productoRepository
        load()
        binding.apply {

            etPresentation.onItemSelectedListener = object : OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    itemSelected = position
                    totalPagar = listaPresentaciones[itemSelected].price!!
                    tvTotal.text = getString(R.string.totalPago,totalPagar.toString())

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            btNext.setOnClickListener {
                if (pedido == null){
                    parentFragmentManager.beginTransaction().
                    replace(R.id.fgContainerView,DireccionFragment.newInstance(
                        producto = producto,
                        pedido = null,
                        presentacion = itemSelected ,
                        totalPagar = totalPagar,
                        /*pedidoId=-1,tipo = tipo,
                        productoId = productoId ,
                        productoImageURL = productoImageURL*/)).
                    addToBackStack(null).
                    commit()
                }
                else{
                    parentFragmentManager.beginTransaction().
                    replace(R.id.fgContainerView,DireccionFragment.newInstance(
                        producto = producto,
                        pedido = pedido,
                        presentacion = itemSelected ,
                        totalPagar = totalPagar,
                        /*pedidoId=pedidoId,
                        tipo = tipo,
                        productoId = productoId ,
                        productoImageURL = productoImageURL*/)).
                    addToBackStack(null).
                    commit()
                }
            }
        }
    }

    private fun load(){
        binding.tvError.visibility = View.GONE
        binding.btReload.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
        binding.tvPresentations.visibility = View.GONE
        binding.tvProduct.visibility = View.GONE
        binding.etPresentation.visibility = View.GONE
        binding.tvTotal.visibility = View.GONE
        binding.btNext.visibility = View.GONE

        lifecycleScope.launch {

            producto.id.let { id ->
                val call: Call<DetalleProductoDto> = productoRepository.getDetalleProductoApiary(id.toString())
                call.enqueue(object : Callback<DetalleProductoDto> {
                    override fun onResponse(
                        call: Call<DetalleProductoDto>,
                        response: Response<DetalleProductoDto>
                    ) {
                        listaPresentaciones.clear()
                        listaPresentacionesNombres.clear()
                        binding.pbLoading.visibility = View.GONE
                        binding.tvPresentations.visibility = View.VISIBLE
                        binding.tvProduct.visibility = View.VISIBLE
                        binding.etPresentation.visibility = View.VISIBLE
                        binding.tvTotal.visibility = View.VISIBLE
                        binding.btNext.visibility = View.VISIBLE

                        for (i in response.body()?.presentations!!.indices) {
                            /*listaPresentaciones.add( getString(
                                R.string.presentacionesConPrecio,
                                response.body()?.presentations?.get(i)?.desc,
                                response.body()?.presentations?.get(i)?.price.toString()
                                )
                            )*/
                            listaPresentaciones.add(response.body()?.presentations?.get(i)!!)
                            listaPresentacionesNombres.add(response.body()?.presentations?.get(i)?.desc!!)
                            //listaPrecios.add(response.body()!!.presentations?.get(i)?.price!!)

                        }
                        binding.apply {
                            tvProduct.text = producto.name
                            etPresentation.adapter = ArrayAdapter(requireContext(),R.layout.spinner_layout,R.id.spinnerText,listaPresentacionesNombres)
                            itemSelected = 0
                            totalPagar = listaPresentaciones[itemSelected].price!!
                            tvTotal.text = getString(R.string.totalPago,totalPagar.toString())
                        }
                    }

                    override fun onFailure(call: Call<DetalleProductoDto>, t: Throwable) {
                        binding.btReload.visibility = View.VISIBLE
                        binding.tvError.visibility = View.VISIBLE
                        binding.btNext.visibility = View.VISIBLE
                        binding.pbLoading.visibility = View.GONE
                        binding.tvPresentations.visibility = View.GONE
                        binding.tvProduct.visibility = View.GONE
                        binding.etPresentation.visibility = View.GONE
                        binding.tvTotal.visibility = View.GONE
                        binding.btReload.setOnClickListener {

                            load()

                        }
                    }
                })
            }
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance(producto: ProductoDto,pedido: PedidoEntity?) = PresentacionFragment(producto = producto, pedido = pedido)/*.apply {
            arguments = Bundle().apply {
                putChar(TIPO,tipo)
                putLong(PRODUCTO_ID, productoId)
                putString(PRODUCTO_IMAGE_URL,productoImageURL)
                putLong(PEDIDO_ID,pedidoId)
            }


        }*/
    }
}