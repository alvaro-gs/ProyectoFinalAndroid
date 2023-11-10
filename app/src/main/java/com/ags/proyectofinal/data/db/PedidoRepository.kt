package com.ags.proyectofinal.data.db

import com.ags.proyectofinal.data.db.model.PedidoEntity
import com.google.firebase.auth.FirebaseAuth

class PedidoRepository(private val pedidoDao: PedidoDao) {
    suspend fun insertPedido(pedido:PedidoEntity){
        pedidoDao.insertPedido(pedido)
    }
    suspend fun insertPedido(productoId:Long, image: String,name: String, userId : String, status : Short,street : String,suburb: String, postalCode: Int,notes: String,remainingPayment:Double,presentation : Int){
        pedidoDao.insertPedido(PedidoEntity(productoId = productoId,userId=userId, name=name, imageURL = image,status=status, street = street, suburb = suburb,postalCode = postalCode, notes = notes, remainingPayment=remainingPayment,presentation=presentation))
    }

    suspend fun getAllPedidosByUser(userId: String) : List<PedidoEntity> = pedidoDao.getAllPedidosByUser(userId)


    suspend fun updatePedido(pedido: PedidoEntity){
        pedidoDao.updatePedido(pedido)
    }

    suspend fun deletePedido (pedido:PedidoEntity){
        pedidoDao.deletePedido(pedido)
    }
}


