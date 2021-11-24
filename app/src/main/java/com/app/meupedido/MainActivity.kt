package com.app.meupedido

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.core.view.isGone
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.adapter.OrderAdapter
import com.app.meupedido.databinding.ActivityMainBinding
import com.app.meupedido.model.Order
import com.app.meupedido.util.SwipeGesture
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : AppCompatActivity() {

    private val REGISTER_ACTIVITY_REQUEST_CODE = 0

    val listOrders = mutableListOf<Order>(
//        Order("RRE1458", "Em Preparação"),
//        Order("RAT1735", "Em Preparação"),
//        Order("RPW1935", "Em Preparação"),
//        Order("RY1635", "Em Preparação"),
//        Order("PA1335", "Em Preparação"),
//        Order("PA1235", "Em Preparação"),
//        Order("SR1285", "Em Preparação"),
//        Order("SB1225", "Em Preparação"),
//        Order("RA1265", "Em Preparação"),
//        Order("RA1295", "Em Preparação"),
//        Order("RA1235", "Em Preparação"),
//        Order("RA1235", "Em Preparação"),
//        Order("RA1235", "Em Preparação"),
//        Order("TR5896", "Em Preparação")
    )

    private val orderAdapter by lazy { OrderAdapter(listOrders) }

    private lateinit var binding: ActivityMainBinding
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvOrders.adapter = orderAdapter
        binding.rvOrders.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(binding.toolbar)

        binding.fabRegister.setOnClickListener { Register() }

        showLabelEmpty()

        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val numberOrder = orderAdapter.orders[viewHolder.adapterPosition].number
                listOrders.removeAt(viewHolder.adapterPosition)
                orderAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                println("Removido $numberOrder")

                Firebase.messaging.unsubscribeFromTopic(numberOrder)

                toastShow(numberOrder, 1)
                showLabelEmpty()
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvOrders)

    }

    private fun toastShow(numberOrder: String, type: Int) {
        if (type == 0)
            Toast.makeText(this,"O pedido $numberOrder foi adicionado",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "O pedido $numberOrder foi removido", Toast.LENGTH_SHORT).show()
    }

    private fun showLabelEmpty() {
        binding.tvListOrdersEmpty.isGone = (orderAdapter.itemCount > 0)
    }

    private fun Register() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE)
        //startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val numberOrderReturn = data!!.getStringExtra("keyName")

                if (!numberOrderReturn.isNullOrEmpty()) {
                    Firebase.messaging.subscribeToTopic(numberOrderReturn)
                        .addOnCompleteListener { task ->
                            var msg = getString(R.string.msg_subscribed)
                            if (!task.isSuccessful) {
                                msg = getString(R.string.msg_subscribe_failed)
                            }
                            Log.d(ContentValues.TAG, msg)
                        }
                    listOrders.add(Order(numberOrderReturn, "em preparação"))
                    orderAdapter.notifyDataSetChanged()
                    showLabelEmpty()
                    toastShow(numberOrderReturn, 0)
                }
            }
        }
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