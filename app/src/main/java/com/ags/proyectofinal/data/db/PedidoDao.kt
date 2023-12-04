package com.ags.proyectofinal.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.ags.proyectofinal.util.Constants.DATABASE_PEDIDO_TABLE
import com.ags.proyectofinal.util.Constants.USER_ID_COLUMN
import com.ags.proyectofinal.util.Constants.PEDIDO_ID_COLUMN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Dao
interface PedidoDao {

    //Create
    @Insert
    suspend fun insertPedido(game: PedidoEntity)

    @Insert
    suspend fun insertPedido(games: List<PedidoEntity>)

    //Read
    @Query("SELECT * FROM ${DATABASE_PEDIDO_TABLE} where ${USER_ID_COLUMN} = :userId")
    suspend fun getAllPedidosByUser(userId: String): List<PedidoEntity>

    @Query("SELECT * FROM ${DATABASE_PEDIDO_TABLE} where ${PEDIDO_ID_COLUMN} = :pedidoId")
    suspend fun getPedidoById(pedidoId:Long) : PedidoEntity

    @Query("SELECT * FROM ${DATABASE_PEDIDO_TABLE}")
    suspend fun getAllPedidos() : List<PedidoEntity>

    //Update
    @Update
    suspend fun updatePedido(game: PedidoEntity)

    //Delete
    @Delete
    suspend fun deletePedido(game: PedidoEntity)
}