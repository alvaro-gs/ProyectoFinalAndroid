package com.ags.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentDetallePedidoBinding
import com.bumptech.glide.Glide


class DetallePedidoFragment(
    private var pedido : PedidoEntity
)
    : Fragment() {

    private  var _binding : FragmentDetallePedidoBinding ? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetallePedidoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var detail = ""
        var status = ""
        if(pedido.postalCode != -1){

            detail = getString(R.string.entregaEn) + " " + pedido.street +  getString(R.string.coma) + " " + pedido.suburb + getString(R.string.coma) + " " + getString(R.string.codigoPostal) + " " + pedido.postalCode.toString() + " " + getString(R.string.notasAdicionales) + " " + pedido.notes

        }
        else{
            detail = getString(R.string.entregaSucursal)
        }

        when(pedido.status){
            0.toShort() -> {
                status = getString(R.string.estatus0)
            }

            1.toShort() -> {
                status = getString(R.string.estatus1)
            }

            2.toShort() -> {
                status = getString(R.string.estatus2)
            }

            3.toShort() -> {
                status = getString(R.string.estatus3)
            }

            4.toShort() -> {
                status = getString(R.string.estatus4)
            }

            5.toShort() -> {
                status = getString(R.string.estatus5)
            }
        }
        binding.apply {
            tvOrderTitle.text = getString(R.string.productoTitulo,pedido.name)
            tvDetail.text = detail
            tvEstatus.text = status
            tvPay.text = pedido.remainingPayment.toString()
            Glide.with(requireContext())
                .load(pedido.imageURL)
                .into(ivProduct)
            /*Picasso.get().load(pedido.imageURL)
                .placeholder(R.drawable.ic_image)
                .into(ivProduct)*/
            if (pedido.observations != ""){
                tvNotes.text = pedido.observations
            }
            else{
                tvNotes.text = getString(R.string.sinObservaciones)
            }


        }


        binding.btReturn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, ListaPedidosFragment.newInstance())
                .commit()
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance(pedido: PedidoEntity) = DetallePedidoFragment(pedido)
    }
}