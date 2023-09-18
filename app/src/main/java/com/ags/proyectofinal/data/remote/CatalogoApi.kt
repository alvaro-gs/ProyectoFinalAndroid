package com.ags.proyectofinal.data.remote

import com.ags.proyectofinal.data.remote.model.DetalleProductoDto
import com.ags.proyectofinal.data.remote.model.ProductoDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogoApi {

    @GET("postres/catalogo_postres")
    fun getCatalogoProductosApiary(): Call<List<ProductoDto>>

    @GET("postres/detalle_postres/{id}")
    fun getDetalleProductoApiary(
        @Path("id") id: String?
    ): Call<DetalleProductoDto>
}