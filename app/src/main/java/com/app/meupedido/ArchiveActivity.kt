package com.app.meupedido

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.meupedido.adapter.ArchivedAdapter
import com.app.meupedido.data.Archived
import com.app.meupedido.databinding.ActivityArchiveBinding
import com.app.meupedido.viewmodel.ArchivedViewModel

class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding

    private val orderArchivedsAdapter = ArchivedAdapter(this, emptyList())

    private lateinit var mArchivedViewModel: ArchivedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mArchivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]
        mArchivedViewModel.readAllData.observe(this, Observer { archived ->
            orderArchivedsAdapter.setData(archived)
            showLabelEmpty()
        })


        binding.rvOrdersArchiveds.adapter = orderArchivedsAdapter
        binding.rvOrdersArchiveds.layoutManager = LinearLayoutManager(this)

        showLabelEmpty()
    }

    fun insertArchivedToDatabase(numberOrder: String) {
        val archived = Archived(
            numberOrder,
            "Em Execução",
            "data",
            "Spoleto",
            "SPL"
        )
        mArchivedViewModel.addArchived(archived)
    }

    private fun showLabelEmpty() {
        binding.tvListOrdersEmpty.isGone = (orderArchivedsAdapter.itemCount > 0)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}