package com.ags.proyectofinal.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.util.Constants.DATABASE_PEDIDO_TABLE

@Dao
interface PedidoDao {
    //Create
    @Insert
    suspend fun insertPedido(game: PedidoEntity)

    @Insert
    suspend fun insertPedido(games: List<PedidoEntity>)

    //Read
    @Query("SELECT * FROM ${DATABASE_PEDIDO_TABLE}")
    suspend fun getAllPedidos(): List<PedidoEntity>

    //Update
    @Update
    suspend fun updatePedido(game: PedidoEntity)

    //Delete
    @Delete
    suspend fun deletePedido(game: PedidoEntity)
}