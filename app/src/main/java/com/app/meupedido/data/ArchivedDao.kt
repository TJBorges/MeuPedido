package com.app.meupedido.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArchivedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArchived(archived: Archived)

    @Query("SELECT * FROM archivedTable ORDER BY number ASC")
    fun readAllData(): LiveData<List<Archived>>

    @Query("SELECT * FROM archivedTable WHERE number = :number")
    fun searchOrderWithNumber(number: String) : LiveData<List<Archived>>
}