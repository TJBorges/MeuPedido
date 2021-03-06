package com.app.meupedido.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM orderTable")
    suspend fun deleteAllOrders()

    @Query("SELECT * FROM orderTable ORDER BY status DESC, date")
    fun readAllData(): LiveData<List<Order>>
}