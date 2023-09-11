package com.ags.proyectofinal.application

import android.app.Application
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.PedidoDatabase

class ProyectoFinalApp():Application() {
    private val database by lazy{
        PedidoDatabase.getDatabase(this@ProyectoFinalApp)
    }
    val repository by lazy{
        PedidoRepository(database.pedidoDao())
    }
}