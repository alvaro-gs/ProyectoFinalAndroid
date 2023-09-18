package com.ags.proyectofinal.application

import android.app.Application
import com.ags.proyectofinal.data.db.PedidoRepository
import com.ags.proyectofinal.data.db.PedidoDatabase
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.RetrofitHelper

class ProyectoFinalApp():Application() {
    private val database by lazy{
        PedidoDatabase.getDatabase(this@ProyectoFinalApp)
    }
    val repository by lazy{
        PedidoRepository(database.pedidoDao())
    }

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val productoRepository by lazy{
        ProductoRepository(retrofit)
    }
}