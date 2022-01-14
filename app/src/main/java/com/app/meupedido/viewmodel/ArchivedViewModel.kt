package com.app.meupedido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.meupedido.data.Archived
import com.app.meupedido.data.OrderDatabase
import com.app.meupedido.repository.ArchivedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArchivedViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Archived>>
    private val repository: ArchivedRepository

    init {
        val archivedDao = OrderDatabase.getDatabase(application).archivedDao()
        repository = ArchivedRepository(archivedDao)
        readAllData = repository.readAllData
    }

    fun addArchived(archived: Archived) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArchived(archived)
        }
    }

    fun searchOrderWithNumber(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchOrderWithNumber(number)
        }
    }
}