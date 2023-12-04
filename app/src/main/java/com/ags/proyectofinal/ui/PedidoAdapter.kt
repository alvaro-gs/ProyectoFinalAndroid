package com.ags.proyectofinal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.databinding.ItemPedidoBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class PedidoAdapter(private val onPedidoClicked: (PedidoEntity, String) -> Unit): RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private var pedidos: List<PedidoEntity> = emptyList()


    class ViewHolder(private val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root){
        val updateIcon = binding.btEdit
        val deleteIcon = binding.btDelete
        val image = binding.ivPedido
        fun bind(estatus : String){

            binding.apply {
                tvEstatus.text = estatus
                /*if(pedido.imageURL != "") {
                    Picasso.get().load(pedido.imageURL)
                        .placeholder(R.drawable.ic_image)
                        .into(ivPedido)
                }*/
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = pedidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pedido = pedidos[position]
        var estatus = ""
        when(pedido.status){
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

        holder.bind(estatus)

        Glide.with(holder.itemView.context)
            .load(pedido.imageURL)
            .into(holder.image)

        holder.updateIcon.setOnClickListener {
            onPedidoClicked(pedidos[position],"Update")
        }
        holder.deleteIcon.setOnClickListener {
            onPedidoClicked(pedidos[position],"Delete")
        }
        holder.image.setOnClickListener {
            onPedidoClicked(pedidos[position],"Detail")
        }

    }
    fun updateList(listaPedidos: List<PedidoEntity>){
        pedidos = listaPedidos
        notifyDataSetChanged()
    }
}

