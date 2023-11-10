package com.ags.proyectofinal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ags.proyectofinal.data.util.OpcionMenu
import com.ags.proyectofinal.databinding.UserMenuItemBinding

class MenuOpcionesAdapter(
    private val opciones: List<OpcionMenu>,
    private val onOpcionClicked : (Int) -> Unit
) : RecyclerView.Adapter<MenuOpcionesAdapter.ViewHolder>()
{
    class ViewHolder(private val binding: UserMenuItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(opcion: OpcionMenu){
            binding.tvOpcion.text = opcion.nombre
            binding.iv.setImageResource(opcion.recursoImagenId)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserMenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = opciones.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(opciones[position])
        holder.itemView.setOnClickListener {
          onOpcionClicked(position)
      }
    }
}