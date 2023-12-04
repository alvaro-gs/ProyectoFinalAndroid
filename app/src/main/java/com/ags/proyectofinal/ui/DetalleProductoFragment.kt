package com.ags.proyectofinal.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.DetalleProductoDto
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.FragmentDetalleProductoBinding
import com.ags.proyectofinal.util.Constants
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PRODUCTO_ID = "producto_id"

class DetalleProductoFragment : Fragment() {

    private var _binding: FragmentDetalleProductoBinding ?= null
    private val binding get() = _binding!!

    private var productoId: Long = -1

    private var productoImageURL: String = ""

    private lateinit var repository: ProductoRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productoId = it.getLong(PRODUCTO_ID,-1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleProductoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvError.visibility = View.GONE
        binding.btReload.visibility = View.GONE
        repository = (requireActivity().application as ProyectoFinalApp).productoRepository
        load()

        binding.btOrder.setOnClickListener {
            parentFragmentManager.beginTransaction().
            replace(R.id.fgContainerView,PresentacionFragment.newInstance(tipo='N',productoId = productoId, productoImageURL = productoImageURL, pedidoId = -1)).
            addToBackStack(null).
            commit()
        }


    }

    private fun load(){
        binding.tvError.visibility = View.GONE
        binding.btReload.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
        binding.tvName.visibility = View.VISIBLE
        binding.ivProduct.visibility = View.VISIBLE
        binding.tvDescriptionTitle.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.tvTypeTitle.visibility = View.VISIBLE
        binding.tvType.visibility = View.VISIBLE
        binding.tvPresentationsTitle.visibility = View.VISIBLE
        binding.tvPresentations.visibility = View.VISIBLE
        binding.btOrder.visibility = View.VISIBLE

        lifecycleScope.launch {
            productoId?.let { id ->
                val call: Call<DetalleProductoDto> = repository.getDetalleProductoApiary(id.toString())
                call.enqueue(object : Callback<DetalleProductoDto> {
                    override fun onResponse(
                        call: Call<DetalleProductoDto>,
                        response: Response<DetalleProductoDto>
                    ) {
                        binding.apply {
                            pbLoading.visibility = View.GONE

                            tvName.text = response.body()?.name

                            productoImageURL = response.body()?.imageURL!!
                            Glide.with(requireContext())
                                .load(response.body()?.imageURL)
                                .into(ivProduct)
                            /*Picasso.get()
                                .load(response.body()?.imageURL)
                                .error(R.drawable.ic_image)
                                .into(ivProduct)*/



                            tvDescription.text = response.body()?.description
                            var category = ""
                            when(response.body()?.categoryId){
                                1.toLong() -> category = getString(R.string.categoria1)
                                2.toLong() -> category = getString(R.string.categoria2)
                                3.toLong() -> category = getString(R.string.categoria3)
                                4.toLong() -> category = getString(R.string.categoria4)
                                5.toLong() -> category = getString(R.string.categoria5)
                            }
                            tvType.text = category

                            var textoPresentation = ""
                            for (i in response.body()?.presentations!!.indices){
                                textoPresentation += getString(R.string.presentacionesConPrecio,
                                    response.body()?.presentations?.get(i)?.desc,
                                    response.body()?.presentations?.get(i)?.price.toString())

                                if (i < (response.body()?.presentations?.size!!) - 1)
                                    textoPresentation += System.getProperty("line.separator")
                            }

                            tvPresentations.text = textoPresentation
                        }
                    }

                    override fun onFailure(call: Call<DetalleProductoDto>, t: Throwable) {
                        binding.pbLoading.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.btReload.visibility = View.VISIBLE
                        binding.tvName.visibility = View.GONE
                        binding.ivProduct.visibility = View.GONE
                        binding.tvDescriptionTitle.visibility = View.GONE
                        binding.tvDescription.visibility = View.GONE
                        binding.tvTypeTitle.visibility = View.GONE
                        binding.tvType.visibility = View.GONE
                        binding.tvPresentationsTitle.visibility = View.GONE
                        binding.tvPresentations.visibility = View.GONE
                        binding.btOrder.visibility = View.GONE

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
        @JvmStatic
        fun newInstance(productoId: Long ) = DetalleProductoFragment().apply {
            arguments = Bundle().apply {
                putLong(PRODUCTO_ID, productoId)
            }
        }
    }
}