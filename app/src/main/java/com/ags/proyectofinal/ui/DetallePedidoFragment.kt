package com.ags.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.FragmentDetallePedidoBinding
import com.squareup.picasso.Picasso


class DetallePedidoFragment(
    private var pedido : PedidoEntity,
    private val updateUI: () -> Unit
)
    : Fragment(R.layout.fragment_detalle_pedido) {

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

            5.toShort() ->{
                status = getString(R.string.estatus5)
            }
        }
        binding.apply {
            tvDetail.text = detail
            tvEstatus.text = status
            tvPay.text = pedido.remainingPayment.toString()
            when(pedido.productoId){
                1.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.pastelzanahoria)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                2.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.pastelredvelvet)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                3.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.pastelplatano)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                4.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.brownie)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                5.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.muffinchocolate)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                6.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.muffinscalabaza)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                7.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.scones)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
                8.toLong()->{
                    Picasso.get().load(pedido.imageURL)
                        .error(R.drawable.tartafrutas)
                        .placeholder(R.drawable.ic_image)
                        .into(ivProduct)
                }
            }

        }


        binding.btReturn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, ListaPedidosFragment.newInstance())
                .addToBackStack("ListaPedidosFragment")
                .commit()
        }
    }


    companion object {
        fun newInstance(pedido: PedidoEntity,updateUI: () -> Unit) = DetallePedidoFragment(pedido,updateUI)
    }
}