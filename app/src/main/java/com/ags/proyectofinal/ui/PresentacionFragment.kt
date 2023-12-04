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
import com.ags.proyectofinal.databinding.FragmentPresentacionBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PRODUCTO_ID = "producto_id"
private const val PRODUCTO_IMAGE_URL = "producto_image_url"
private const val PEDIDO_ID = "pedido_id"
private const val TIPO = "tipo"
class PresentacionFragment : Fragment() {


    private var listaPresentaciones: MutableList<String> = emptyList<String>().toMutableList()
    private var listaPrecios: MutableList<Double> = emptyList<Double>().toMutableList()
    private var _binding: FragmentPresentacionBinding?= null
    private val binding get() = _binding!!

    private var productoId: Long = -1

    private var productoImageURL: String = ""

    private var pedidoId: Long = -1

    private var tipo: Char = '_'

    private var itemSelected : Int = 0

    private var totalPagar : Double = 0.0

    private lateinit var productoRepository: ProductoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            productoId = it.getLong(PRODUCTO_ID,-1)
            productoImageURL = it.getString(PRODUCTO_IMAGE_URL,"")
            tipo = it.getChar(TIPO,'_')
            pedidoId = it.getLong(PEDIDO_ID,-1)
        }
    }
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
                    totalPagar = listaPrecios[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            btNext.setOnClickListener {
                if (tipo == 'N'){
                    parentFragmentManager.beginTransaction().
                    replace(R.id.fgContainerView,DireccionFragment.newInstance(tipo = tipo, productoId = productoId ,productoImageURL = productoImageURL, presentacion = itemSelected , totalPagar = totalPagar, pedidoId=-1)).
                    addToBackStack(null).
                    commit()
                }
                else{
                    parentFragmentManager.beginTransaction().
                    replace(R.id.fgContainerView,DireccionFragment.newInstance(tipo = tipo, productoId = productoId ,productoImageURL = productoImageURL, presentacion = itemSelected , totalPagar = totalPagar, pedidoId=pedidoId)).
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
        binding.etPresentation.visibility = View.GONE
        binding.btNext.visibility = View.GONE

        Toast.makeText(requireContext(), "IDDDD3: ${productoId}", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {

            productoId?.let { id ->
                val call: Call<DetalleProductoDto> = productoRepository.getDetalleProductoApiary(id.toString())
                call.enqueue(object : Callback<DetalleProductoDto> {
                    override fun onResponse(
                        call: Call<DetalleProductoDto>,
                        response: Response<DetalleProductoDto>
                    ) {
                        binding.pbLoading.visibility = View.GONE
                        binding.tvPresentations.visibility = View.VISIBLE
                        binding.etPresentation.visibility = View.VISIBLE
                        binding.btNext.visibility = View.VISIBLE

                        for (i in response.body()?.presentations!!.indices) {
                            listaPresentaciones.add( getString(
                                R.string.presentacionesConPrecio,
                                response.body()?.presentations?.get(i)?.desc,
                                response.body()?.presentations?.get(i)?.price.toString()
                                )
                            )
                            listaPrecios.add(response.body()!!.presentations?.get(i)?.price!!)

                        }
                        binding.apply {
                            etPresentation
                            etPresentation.adapter = ArrayAdapter(requireContext(),R.layout.spinner_layout,R.id.spinnerText,listaPresentaciones)
                        }
                    }

                    override fun onFailure(call: Call<DetalleProductoDto>, t: Throwable) {
                        binding.btReload.visibility = View.VISIBLE
                        binding.tvError.visibility = View.VISIBLE
                        binding.btNext.visibility = View.VISIBLE
                        binding.pbLoading.visibility = View.GONE
                        binding.tvPresentations.visibility = View.GONE
                        binding.etPresentation.visibility = View.GONE

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
        fun newInstance(tipo:Char, productoId:Long,productoImageURL: String,pedidoId: Long) = PresentacionFragment().apply {
            arguments = Bundle().apply {
                putChar(TIPO,tipo)
                putLong(PRODUCTO_ID, productoId)
                putString(PRODUCTO_IMAGE_URL,productoImageURL)
                putLong(PEDIDO_ID,pedidoId)
            }


        }
    }
}