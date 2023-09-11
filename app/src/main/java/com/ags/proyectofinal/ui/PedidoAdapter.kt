package com.ags.proyectofinal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.ItemPedidoBinding
import com.squareup.picasso.Picasso


class PedidoAdapter(private val onPedidoClicked: (PedidoEntity, String) -> Unit): RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private var pedidos: List<PedidoEntity> = emptyList()


    class ViewHolder(private val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root){
        val updateIcon = binding.btEdit
        val deleteIcon = binding.btDelete
        val detail = binding.ivPedido
        fun bind(pedido: PedidoEntity,estatus : String){

            binding.apply {
                tvEstatus.text = estatus
                if(pedido.imageURL != "") {
                    when (pedido.productoId) {
                        1.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.pastelzanahoria)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        2.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.pastelredvelvet)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        3.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.pastelplatano)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        4.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.brownie)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        5.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.muffinchocolate)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        6.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.muffinscalabaza)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        7.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.scones)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }

                        8.toLong() -> {
                            Picasso.get().load(pedido.imageURL)
                                .error(R.drawable.tartafrutas)
                                .placeholder(R.drawable.ic_image)
                                .into(ivPedido)
                        }
                    }

                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pedidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var estatus = ""
        when(pedidos[position].status){
            0.toShort() -> {
               estatus = holder.itemView.context.getString(R.string.estatus0)
            }

            1.toShort() -> {
                estatus = holder.itemView.context.getString(R.string.estatus1)
            }

            2.toShort() -> {
                estatus = holder.itemView.context.getString(R.string.estatus2)
            }

            3.toShort() -> {
                estatus = holder.itemView.context.getString(R.string.estatus3)
            }

            4.toShort() -> {
                estatus = holder.itemView.context.getString(R.string.estatus4)
            }

            5.toShort() ->{
                estatus = holder.itemView.context.getString(R.string.estatus5)
            }
        }
        holder.bind(pedidos[position],estatus)

        holder.updateIcon.setOnClickListener {
            onPedidoClicked(pedidos[position],"Update")
        }
        holder.deleteIcon.setOnClickListener {
            onPedidoClicked(pedidos[position],"Delete")
        }
        holder.detail.setOnClickListener {
            onPedidoClicked(pedidos[position],"Detail")
        }

    }
    fun updateList(listaPedidos: List<PedidoEntity>){
        pedidos = listaPedidos
        notifyDataSetChanged()
    }
}

