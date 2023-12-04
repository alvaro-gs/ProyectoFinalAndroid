package com.ags.proyectofinal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.ItemProductoBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class ProductoAdapter(
    private val productos : List<ProductoDto>,
    private val onProductoClicked:  (ProductoDto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ItemProductoBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivThumbnail
        fun bind(producto: ProductoDto) {
            binding.tvName.text  = producto.name
            /*Picasso.get()
                .load(producto.imageURL)
                .error(R.drawable.ic_image)
                .into(binding.ivThumbnail)*/

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)

        Glide.with(holder.itemView.context)
            .load(producto.imageURL)
            .into(holder.image)

        holder.itemView.setOnClickListener{
            onProductoClicked(producto)
        }

    }
}