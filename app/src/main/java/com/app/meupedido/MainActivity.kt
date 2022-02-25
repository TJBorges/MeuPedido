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
import com.app.meupedido.util.DataStore
import com.app.meupedido.util.DateUtil
import com.app.meupedido.util.SwipeGesture
import com.app.meupedido.viewmodel.ArchivedViewModel
import com.app.meupedido.viewmodel.OrderViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : AppCompatActivity() {

    private val REGISTER_ACTIVITY_REQUEST_CODE = 0

    private lateinit var binding: ActivityMainBinding

    private var orderAdapter = OrderAdapter(this, emptyList())
    private lateinit var mOrderViewModel: OrderViewModel
    private lateinit var mArchivedViewModel: ArchivedViewModel
    private val dateUtil = DateUtil()
    private val nameStore = DataStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mOrderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        loadAdapter()

        mArchivedViewModel = ViewModelProvider(this)[ArchivedViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        binding.fabRegister.setOnClickListener { register() }

        swipeGesture()
    }

    private fun loadAdapter() {
        mOrderViewModel.readAllData.observe(this, androidx.lifecycle.Observer { order ->
            orderAdapter.setData(order)
            showLabelEmpty()

            for (item in order.indices)
                if (order[item].date == dateUtil.getCurrentDateTime() && order[item].status == "Pronto")
                    showDialogNotification(order[item].number)
        })
        binding.rvOrders.adapter = orderAdapter
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
    }

    private fun swipeGesture() {
        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {

                    ItemTouchHelper.RIGHT -> {
                        val numberOrder = orderAdapter.orderList[viewHolder.adapterPosition].number
                        if (orderAdapter.orderList[viewHolder.adapterPosition].status == "Em execução") {

                            var builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(R.string.order_delete)
                            builder.setMessage(getString(R.string.dialog_delete, numberOrder))
                            builder.setPositiveButton("Sim") { dialog, id ->
                                mOrderViewModel.removeOrderToDatabase(numberOrder)
                                orderAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                                Log.d("MainActivity", "Removido $numberOrder")

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
                        } else {
                            var builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(R.string.order_delete)
                            builder.setMessage(R.string.dialog_not_delete)
                            builder.setPositiveButton("OK") { dialog, id ->
                                orderAdapter.notifyDataSetChanged()
                                dialog.cancel()
                            }
                            builder.setCancelable(false)
                            var alert = builder.create()
                            alert.show()
                        }
                    }
                    ItemTouchHelper.LEFT -> {
                        val numberOrder = orderAdapter.orderList[viewHolder.adapterPosition].number
                        if (orderAdapter.orderList[viewHolder.adapterPosition].status == "Pronto") {

                            var builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(R.string.order_archive)
                            builder.setMessage(getString(R.string.dialog_archived, numberOrder))
                            builder.setPositiveButton("Sim") { dialog, id ->
                                mOrderViewModel.removeOrderToDatabase(numberOrder)
                                mArchivedViewModel.insertArchivedToDatabase(numberOrder)
                                Firebase.messaging.unsubscribeFromTopic(numberOrder)

                                toastShow(numberOrder, 3)
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
                        } else {
                            var builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(R.string.order_archive)
                            builder.setMessage(R.string.dialog_not_archived)
                            builder.setPositiveButton("OK") { dialog, id ->
                                orderAdapter.notifyDataSetChanged()
                                dialog.cancel()
                            }
                            builder.setCancelable(false)
                            var alert = builder.create()
                            alert.show()
                        }
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvOrders)

        showLabelEmpty()
    }

    private fun showDialogNotification(number: String) {
        val nameStore = nameStore.name(number.substring(0, 3))
        var builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(getString(R.string.dialog_notification_title, number, nameStore))
        builder.setMessage(getString(R.string.dialog_notification_message))
        builder.setPositiveButton("Sim") { dialog, id ->
            mArchivedViewModel.insertArchivedToDatabase(number)
            mOrderViewModel.removeOrderToDatabase(number)
            Firebase.messaging.unsubscribeFromTopic(number)

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

    private fun toastShow(numberOrder: String, type: Int) {
        when (type) {
            0 -> Toast.makeText(this,
                getString(R.string.toast_add, numberOrder),
                Toast.LENGTH_SHORT)
                .show()
            1 -> Toast.makeText(this,
                getString(R.string.toast_remove, numberOrder),
                Toast.LENGTH_SHORT)
                .show()
            else -> Toast.makeText(this,
                getString(R.string.toast_archived, numberOrder),
                Toast.LENGTH_SHORT)
                .show()
        }
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

                    mOrderViewModel.insertOrderToDatabase(numberOrderReturn,
                        getString(R.string.order_status_in_progress))

                    orderAdapter.notifyDataSetChanged()
                    showLabelEmpty()
                    toastShow(numberOrderReturn, 0)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btArchived -> goToArchived("")
            R.id.btSettings -> goToSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToArchived(numberOrder: String) {
        val intent = Intent(this, ArchiveActivity::class.java)
        intent.putExtra("numberOrder", numberOrder)
        startActivity(intent)
    }

    private fun goToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}