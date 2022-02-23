package com.app.meupedido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.meupedido.data.Order
import com.app.meupedido.data.OrderDatabase
import com.app.meupedido.repository.OrderRepository
import com.app.meupedido.util.DataStore
import com.app.meupedido.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Order>>
    private val repository: OrderRepository
    private val dateUtil = DateUtil()
    private val nameStore = DataStore()

    init {
        val orderDao = OrderDatabase.getDatabase(application).orderDao()
        repository = OrderRepository(orderDao)
        readAllData = repository.readAllData
    }

    private fun addOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

    private fun deleteOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrder(order)
        }
    }

    fun deleteAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllOrders()
        }
    }

    fun insertOrderToDatabase(numberOrderReturn: String, status: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrderReturn.substring(0, 3)
        val nameStore = nameStore.name(icon)
        val order = Order(
            number = numberOrderReturn,
            status = status,
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        addOrder(order)
    }

    fun removeOrderToDatabase(numberOrder: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrder.substring(0, 3)
        val nameStore = nameStore.name(icon)
        val order = Order(
            number = numberOrder,
            status = "Em execução",
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        deleteOrder(order)
    }


}