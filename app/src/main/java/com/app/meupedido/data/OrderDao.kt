package com.app.meupedido.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: Order)

    @Query("SELECT * FROM orderTable ORDER BY number ASC")
    fun readAllData(): LiveData<List<Order>>
}