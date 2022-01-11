package com.app.meupedido

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.adapter.OrderAdapter
import com.app.meupedido.data.Order
import com.app.meupedido.data.OrderViewModel
import com.app.meupedido.databinding.ActivityMainBinding
import com.app.meupedido.util.SwipeGesture
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REGISTER_ACTIVITY_REQUEST_CODE = 0
    private val ARCHIVED_ORDER = "ARCHIVED_ORDER"
    //private var sqlLiteHelper = SQLiteHelper

    //val listOrders = List<Order>(
//        Order("RRE1458", "Em execução"),
//        Order("RAT1735", "Em execução"),
//        Order("RPW1935", "Em execução"),
//        Order("RYO1635", "Em execução"),
//        Order("PAT1335", "Em execução"),
//        Order("PA1235", "Em execução"),
//        Order("SR1285", "Em execução"),
//        Order("SB1225", "Em execução"),
//        Order("RA1265", "Em execução"),
//        Order("RA1295", "Em execução"),
//        Order("RA1235", "Em execução"),
//        Order("RA1235", "Em execução"),
//        Order("RA1235", "Em execução"),
//        Order("TR5896", "Em execução")

    private val orderAdapter = OrderAdapter(emptyList())

    private lateinit var binding: ActivityMainBinding
    private lateinit var mOrderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mOrderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        mOrderViewModel.readAllData.observe(this, androidx.lifecycle.Observer { order ->
            orderAdapter.setData(order)
        })

        binding.rvOrders.adapter = orderAdapter
        binding.rvOrders.layoutManager = LinearLayoutManager(this)


        setSupportActionBar(binding.toolbar)

        binding.fabRegister.setOnClickListener { Register() }



        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val numberOrder = orderAdapter.orders[viewHolder.adapterPosition].number

                var builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle(R.string.order_delete)
                builder.setMessage("Deseja excluir o pedido $numberOrder da sua lista?")
                builder.setPositiveButton("Sim", DialogInterface.OnClickListener { dialog, id ->
                    //listOrders. removeAt(viewHolder.adapterPosition)
                    orderAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    println("Removido $numberOrder")

                    Firebase.messaging.unsubscribeFromTopic(numberOrder)

                    toastShow(numberOrder, 1)
                    showLabelEmpty()
                    dialog.cancel()
                    //goToArchived(numberOrder)
                })
                builder.setNegativeButton("Não", DialogInterface.OnClickListener { dialog, id ->
                    orderAdapter.notifyDataSetChanged()
                    dialog.cancel()
                })
                var alert = builder.create()
                alert.show()

            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvOrders)

        showLabelEmpty()
    }

    private fun toastShow(numberOrder: String, type: Int) {
        if (type == 0)
            Toast.makeText(this, "O pedido $numberOrder foi adicionado", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "O pedido $numberOrder foi removido", Toast.LENGTH_SHORT).show()
    }

    private fun showLabelEmpty() {
        val itemCount = orderAdapter.itemCount
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
//                    val order = Order(numberOrderReturn, getString(R.string.order_status_in_progress), getCurrentDateTime())
//                    val statusInsert = sqlLiteHelper.insertOrder(order)

                    insertDataToDatabase(numberOrderReturn)
//                    listOrders.add(
//                        Order(
//                            numberOrderReturn,
//                            getString(R.string.order_status_in_progress)
//                        )
//                    )// getCurrentDateTime()))
                    orderAdapter.notifyDataSetChanged()
                    showLabelEmpty()
                    toastShow(numberOrderReturn, 0)
                }
            }
        }
    }

    private fun insertDataToDatabase(numberOrderReturn: String) {
        val order = com.app.meupedido.data.Order(
            0,
            numberOrderReturn,
            "Em Execução",
            "data",
            "Spoleto",
            "SPL"
        )
        mOrderViewModel.addOrder(order)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btArchived -> goToArchived("")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToArchived(numberOrder: String) {
        val intent = Intent(this, ArchiveActivity::class.java)
        intent.putExtra("numberOrder", numberOrder)
        startActivity(intent)
    }

    fun getCurrentDateTime(): String {
        return Calendar.getInstance().time.toString()
    }
}