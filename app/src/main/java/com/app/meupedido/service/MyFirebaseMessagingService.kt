package com.app.meupedido.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.app.meupedido.ArchiveActivity
import com.app.meupedido.MainActivity
import com.app.meupedido.R
import com.app.meupedido.util.DataStore
import com.app.meupedido.util.ValidateInsertOrder
import com.app.meupedido.viewmodel.ArchivedViewModel
import com.app.meupedido.viewmodel.OrderViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channeName = "com.app.meupedido"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var mArchivedViewModel: ArchivedViewModel
    private lateinit var mOrderViewModel: OrderViewModel
    private val dataStore = DataStore()
    private val validateInsertOrder = ValidateInsertOrder()
    private lateinit var prefs: SharedPreferences
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

        var intent = Intent(this, MainActivity::class.java)
        prefs = this.getSharedPreferences("my_orders_prefs", 0)
        val number = title.substring(11, 19).trim().uppercase()
        val logo = dataStore.logo(number.substring(0, 3))

        mOrderViewModel.removeOrderToDatabase(number)

        if (prefs.getBoolean("auto_archive_unit", false)) {
            mArchivedViewModel.insertArchivedToDatabase(number)
            intent = Intent(this, ArchiveActivity::class.java)
        } else
            mOrderViewModel.insertOrderToDatabase(number, getString(R.string.order_status_done))

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
}