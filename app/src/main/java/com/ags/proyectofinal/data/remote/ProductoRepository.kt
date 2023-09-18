package com.ags.proyectofinal.data.remote

import com.ags.proyectofinal.data.remote.model.DetalleProductoDto
import com.ags.proyectofinal.data.remote.model.ProductoDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create

class ProductoRepository(private val retrofit: Retrofit) {
    private val catalogoApi: CatalogoApi = retrofit.create(CatalogoApi::class.java)

    fun getCatalogoProductosApiary(): Call<List<ProductoDto>> =
        catalogoApi.getCatalogoProductosApiary()

    fun getDetalleProductoApiary(id:String?):Call<DetalleProductoDto> =
        catalogoApi.getDetalleProductoApiary(id)
}
