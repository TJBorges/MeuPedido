package com.app.meupedido.repository

import androidx.lifecycle.LiveData
import com.app.meupedido.data.Archived
import com.app.meupedido.data.ArchivedDao

class ArchivedRepository(private  val archivedDao: ArchivedDao) {

    val readAllData: LiveData<List<Archived>> = archivedDao.readAllData()

    suspend fun addArchived(archived: Archived) {
        archivedDao.addArchived(archived)
    }

    fun searchOrderWithNumber(number: String) {
        archivedDao.searchOrderWithNumber(number)
    }
}