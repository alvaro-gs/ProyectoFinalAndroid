package com.ags.proyectofinal.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentListaPedidosBinding
import com.ags.proyectofinal.databinding.FragmentListaProductosBinding
import com.ags.proyectofinal.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListaProductosFragment : Fragment() {

    private var _binding: FragmentListaProductosBinding ?= null
    private val binding get() = _binding!!

    private lateinit var repository: ProductoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaProductosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity() .application as ProyectoFinalApp).productoRepository
        lifecycleScope.launch {
            val call: Call<List<ProductoDto>> = repository.getCatalogoProductosApiary()

            call.enqueue(object: Callback<List<ProductoDto>> {
                override fun onResponse(
                    call: Call<List<ProductoDto>>,
                    response: Response<List<ProductoDto>>
                ) {
                    binding.pbLoading.visibility = View.GONE
                    Log.d(Constants.LOGTAG, "Respuesta del servidor ${response.body()}")

                    response.body()?.let {productos ->
                        binding.listaProductos.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = ProductoAdapter(productos){ producto ->
                                producto.id?.let { id ->
                                    // Conexión a los detalles
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fgContainerView,DetalleProductoFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                            addItemDecoration(DividerItemDecoration(requireContext(),1))
                        }
                    }
                }

                override fun onFailure(call: Call<List<ProductoDto>>, t: Throwable) {
                    //Manejo del error
                    binding.pbLoading.visibility = View.GONE
                    Log.d(Constants.LOGTAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(), getString(R.string.errorConexion), Toast.LENGTH_LONG).show()
                }

            })

        }
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }
    companion object {

        fun newInstance() = ListaProductosFragment()
    }
}