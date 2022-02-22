package com.app.meupedido.service

import android.app.*
import android.content.Intent
import android.content.res.TypedArray
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.app.meupedido.ArchiveActivity
import com.app.meupedido.MainActivity
import com.app.meupedido.R
import com.app.meupedido.data.Archived
import com.app.meupedido.data.Order
import com.app.meupedido.util.DataStore
import com.app.meupedido.util.DateUtil
import com.app.meupedido.util.ValidateInsertOrder
import com.app.meupedido.viewmodel.ArchivedViewModel
import com.app.meupedido.viewmodel.OrderViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

const val channelId = "notification_channel"
const val channeName = "com.app.meupedido"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var mArchivedViewModel: ArchivedViewModel
    private lateinit var mOrderViewModel: OrderViewModel
    private val dateUtil = DateUtil()
    private val dataStore = DataStore()
    private val validateInsertOrder = ValidateInsertOrder()
    private val logoStore: TypedArray by lazy {
        applicationContext.resources.obtainTypedArray(R.array.logo_stores)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TOKEN:", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        mArchivedViewModel = ArchivedViewModel(Application())
        mOrderViewModel = OrderViewModel(Application())

        if (remoteMessage.notification != null) {
            remoteMessage.notification!!.title?.let {
                val number = it.substring(11, 19).uppercase()
                if (validateInsertOrder.validateNumberOrder(number)) {
                    generateNotification(
                        it,
                        remoteMessage.notification!!.body!!
                    )
                }
            }
        } else if (remoteMessage.data.isNotEmpty()) {
            val data: Map<String, String> = remoteMessage.data
            if (data["title"].toString().isNotEmpty()) {
                val number = data["title"]?.substring(11, 19)?.uppercase() ?: ""
                if (validateInsertOrder.validateNumberOrder(number)) {
                    val title = data["title"]
                    val description = data["description"]
                    generateNotification(title ?: "", description ?: "")
                }
            }
        }
    }

    private fun getRemoteView(title: String, description: String, logo: Int): RemoteViews {

        val remoteView = RemoteViews("com.app.meupedido", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, description)
        remoteView.setImageViewResource(R.id.app_logo, logoStore.getResourceId(logo, 0))

        return remoteView
    }

    private fun generateNotification(title: String, description: String) {

        val number = title.substring(11, 19).trim().uppercase()
        val logo = dataStore.logo(number.substring(0, 3))

        //insertArchivedToDatabase(number)
        removeOrderToDatabase(number)
        insertOrderToDatabase(number)


        val intent = Intent(this, ArchiveActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, description, logo))

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channeName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())

    }

    private fun insertArchivedToDatabase(numberOrder: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrder.substring(0, 3)
        val nameStore = dataStore.name(icon)
        val archived = Archived(
            number = numberOrder,
            status = getString(R.string.order_status_done),
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        mArchivedViewModel.addArchived(archived)
    }

    private fun insertOrderToDatabase(numberOrder: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrder.substring(0, 3)
        val nameStore = dataStore.name(icon)
        val order = Order(
            number = numberOrder,
            status = getString(R.string.order_status_done),
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        mOrderViewModel.addOrder(order)
    }

    private fun removeOrderToDatabase(numberOrder: String) {
        val date = dateUtil.getCurrentDateTime()
        val icon = numberOrder.substring(0, 3)
        val nameStore = dataStore.name(icon)
        val order = Order(
            number = numberOrder,
            status = getString(R.string.order_status_in_progress),
            date = date,
            nameStore = nameStore,
            icon = icon
        )
        mOrderViewModel.deleteOrder(order)
        Firebase.messaging.unsubscribeFromTopic(numberOrder)
    }
}