package com.ags.proyectofinal.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ags.proyectofinal.util.Constants

@Entity  (tableName = Constants.DATABASE_PEDIDO_TABLE)
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pedido_id")
    val id: Long = 0,

    @ColumnInfo(name = "pedido_producto_id")
    var productoId: Long = 0,

    @ColumnInfo(name = "pedido_user_id")
    var userId: Long = 0,

    @ColumnInfo(name = "pedido_name")
    var name: String,

    @ColumnInfo(name = "pedido_image_url")
    var imageURL: String,

    @ColumnInfo(name = "pedido_status")
    var status: Short = 0,

    @ColumnInfo(name = "pedido_street")
    var street: String,

    @ColumnInfo(name = "pedido_suburb")
    var suburb: String,

    @ColumnInfo(name = "pedido_postal_code")
    var postalCode: Int,

    @ColumnInfo(name = "pedido_notes")
    var notes: String ,

    @ColumnInfo(name = "pedido_remaining_payment")
    var remainingPayment: Double,

    @ColumnInfo(name = "pedido_presentation")
    var presentation: Int

)

