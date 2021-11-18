package com.app.meupedido

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.app.meupedido.adapter.OrderAdapter
import com.app.meupedido.databinding.ActivityMainBinding
import com.app.meupedido.model.Order
import com.app.pedidopronto.ArchiveActivity
import com.app.pedidopronto.RegistrationActivity

class MainActivity : AppCompatActivity() {

    val listOrders = mutableListOf(
        Order("RA1458", "Em Preparação"),
        Order("RA1735", "Em Preparação"),
        Order("RP1935", "Em Preparação"),
        Order("RY1635", "Em Preparação"),
        Order("PA1335", "Em Preparação"),
        Order("PA1235", "Em Preparação"),
        Order("SR1285", "Em Preparação"),
        Order("SB1225", "Em Preparação"),
        Order("RA1265", "Em Preparação"),
        Order("RA1295", "Em Preparação"),
        Order("RA1235", "Em Preparação"),
        Order("RA1235", "Em Preparação"),
        Order("RA1235", "Em Preparação"),
        Order("TR5896", "Em Preparação")
    )

    private val mOrderAdapter by lazy { OrderAdapter(this, listOrders) }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvOrders.adapter = mOrderAdapter

        setSupportActionBar(binding.toolbar)

        binding.fabRegister.setOnClickListener { Register() }

        ShowLabelEmpty()
    }

    private fun ShowLabelEmpty() {
        binding.tvListOrdersEmpty.isGone = (mOrderAdapter.count > 0)
    }

    private fun Register() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btArchived -> goToArchived()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToArchived() {
        val intent = Intent(this, ArchiveActivity::class.java)
        startActivity(intent)
    }
}