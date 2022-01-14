package com.app.meupedido

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.meupedido.adapter.OrderAdapter
import com.app.meupedido.databinding.ActivityMainBinding
import com.app.meupedido.util.SwipeGesture
import com.app.meupedido.viewmodel.ArchivedViewModel
import com.app.meupedido.viewmodel.OrderViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REGISTER_ACTIVITY_REQUEST_CODE = 0

    private lateinit var binding: ActivityMainBinding

    private var orderAdapter = OrderAdapter(emptyList())
    private lateinit var mOrderViewModel: OrderViewModel
    private lateinit var mArchivedViewModel: ArchivedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mOrderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        mOrderViewModel.readAllData.observe(this, androidx.lifecycle.Observer { order ->
            orderAdapter.setData(order)
            showLabelEmpty()
        })

        mArchivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]

        binding.rvOrders.adapter = orderAdapter
        binding.rvOrders.layoutManager = LinearLayoutManager(this)


        setSupportActionBar(binding.toolbar)

        binding.fabRegister.setOnClickListener { register() }


        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val numberOrder = orderAdapter.orderList[viewHolder.adapterPosition].number

                var builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle(R.string.order_delete)
                builder.setMessage("Deseja excluir o pedido $numberOrder da sua lista?")
                builder.setPositiveButton("Sim") { dialog, id ->
                    mOrderViewModel.deleteOrder(orderAdapter.orderList[viewHolder.adapterPosition])
                    orderAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    println("Removido $numberOrder")

                    Firebase.messaging.unsubscribeFromTopic(numberOrder)

                    toastShow(numberOrder, 1)
                    showLabelEmpty()
                    dialog.cancel()
                }
                builder.setNegativeButton("Não") { dialog, id ->
                    orderAdapter.notifyDataSetChanged()
                    dialog.cancel()
                }
                builder.setCancelable(false)
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
        binding.tvListOrdersEmpty.isGone = (orderAdapter.itemCount > 0)
    }

    private fun register() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val numberOrderReturn = data!!.getStringExtra("keyName")?.uppercase()

                if (!numberOrderReturn.isNullOrEmpty()) {
                    Firebase.messaging.subscribeToTopic(numberOrderReturn)
                        .addOnCompleteListener { task ->
                            var msg = getString(R.string.msg_subscribed)
                            if (!task.isSuccessful) {
                                msg = getString(R.string.msg_subscribe_failed)
                            }
                            Log.d(ContentValues.TAG, msg)
                        }

                    insertDataToDatabase(numberOrderReturn)

                    orderAdapter.notifyDataSetChanged()
                    showLabelEmpty()
                    toastShow(numberOrderReturn, 0)
                }
            }
        }
    }

    private fun insertDataToDatabase(numberOrderReturn: String) {
        val order = com.app.meupedido.data.Order(
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