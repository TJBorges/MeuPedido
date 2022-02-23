package com.app.meupedido.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.meupedido.data.Archived
import com.app.meupedido.data.OrderDatabase
import com.app.meupedido.repository.ArchivedRepository
import com.app.meupedido.util.DataStore
import com.app.meupedido.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArchivedViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Archived>>
    private val repository: ArchivedRepository
    private val dateUtil = DateUtil()
    private val nameStore = DataStore()

    init {
        val archivedDao = OrderDatabase.getDatabase(application).archivedDao()
        repository = ArchivedRepository(archivedDao)
        readAllData = repository.readAllData
    }

    private fun addArchived(archived: Archived) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArchived(archived)
        }
    }

    fun searchOrderWithNumber(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchOrderWithNumber(number)
        }
    }

    fun insertArchivedToDatabase(numberOrder: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrder.substring(0, 3)
        val nameStore = nameStore.name(icon)
        val archived = Archived(
            number = numberOrder,
            status = "Pronto",
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        addArchived(archived)
    }
}