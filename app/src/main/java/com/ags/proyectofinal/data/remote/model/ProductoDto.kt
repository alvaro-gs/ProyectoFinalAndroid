package com.ags.proyectofinal.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("id")
    var id : String? = null,
    @SerializedName("image")
    var imageURL: String ? = null,
    @SerializedName("name")
    var name: String ? = null
)
