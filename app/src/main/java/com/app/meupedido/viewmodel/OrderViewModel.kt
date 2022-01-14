package com.app.meupedido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.meupedido.data.Order
import com.app.meupedido.data.OrderDatabase
import com.app.meupedido.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Order>>
    private val repository: OrderRepository

    init {
        val orderDao = OrderDatabase.getDatabase(application).orderDao()
        repository = OrderRepository(orderDao)
        readAllData = repository.readAllData
    }

    fun addOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrder(order)
        }
    }

    fun deleteAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllOrders()
        }
    }
}