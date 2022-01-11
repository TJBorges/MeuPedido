package com.app.meupedido.data

import androidx.lifecycle.LiveData

class OrderRepository(private  val orderDao: OrderDao) {

    val readAllData: LiveData<List<Order>> = orderDao.readAllData()

    suspend fun addOrder(order: Order){
        orderDao.addOrder(order)
    }
}