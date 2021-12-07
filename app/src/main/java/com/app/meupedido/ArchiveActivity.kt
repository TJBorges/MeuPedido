package com.app.meupedido

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.meupedido.adapter.OrderArchivedAdapter
import com.app.meupedido.databinding.ActivityArchiveBinding
import com.app.meupedido.model.Order

class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding

    val listOrdersArchiveds = mutableListOf<Order>(
        Order("QTY1458","Pronto"),
        Order("RTY7535","Pronto"),
        Order("IOU1935","Pronto"),
        Order("POY6365","Pronto"),
        Order("WDV1335","Pronto"),
//        Order("PA1235", "Pronto"),
//        Order("SR1285", "Pronto"),
//        Order("SB1225", "Pronto"),
//        Order("RA1265", "Pronto"),
//        Order("RA1295", "Pronto"),
//        Order("RA1235", "Pronto"),
//        Order("RA1235", "Pronto"),
//        Order("RA1235", "Pronto"),
//        Order("TR5896", "Pronto")
    )

    private val orderArchivedsAdapter by lazy { OrderArchivedAdapter(listOrdersArchiveds) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = intent
        val numberOrder: String = (intent.getSerializableExtra("numberOrder") ?: "") as String
        if (numberOrder.isNotEmpty())
            listOrdersArchiveds.add(Order(numberOrder, getString(R.string.order_status_done)))

        binding.rvOrdersArchiveds.adapter = orderArchivedsAdapter
        binding.rvOrdersArchiveds.layoutManager = LinearLayoutManager(this)

        showLabelEmpty()
    }

    private fun showLabelEmpty() {
        binding.tvListOrdersEmpty.isGone = (orderArchivedsAdapter.itemCount > 0)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}