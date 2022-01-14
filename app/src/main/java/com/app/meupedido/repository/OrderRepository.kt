package com.app.meupedido.repository

import androidx.lifecycle.LiveData
import com.app.meupedido.data.Order
import com.app.meupedido.data.OrderDao

class OrderRepository(private val orderDao: OrderDao) {

    val readAllData: LiveData<List<Order>> = orderDao.readAllData()

    suspend fun addOrder(order: Order) {
        orderDao.addOrder(order)
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    suspend fun deleteAllOrders() {
        orderDao.deleteAllOrders()
    }
}