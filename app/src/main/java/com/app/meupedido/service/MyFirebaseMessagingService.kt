package com.app.meupedido.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.app.meupedido.ArchiveActivity
import com.app.meupedido.R
import com.app.meupedido.data.Archived
import com.app.meupedido.data.Order
import com.app.meupedido.viewmodel.ArchivedViewModel
import com.app.meupedido.viewmodel.OrderViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

const val channelId = "notification_channel"
const val channeName = "com.app.meupedido"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var  mArchivedViewModel: ArchivedViewModel
    private lateinit var  mOrderViewModel: OrderViewModel

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        mArchivedViewModel = ArchivedViewModel(Application())
        mOrderViewModel = OrderViewModel(Application())

        if (remoteMessage.notification != null) {
            remoteMessage.notification!!.title?.let {
                generateNotification(
                    it,
                    remoteMessage.notification!!.body!!
                )
            }
        }

    }

    private fun getRemoteView(title: String, description: String): RemoteViews {

        val remoteView = RemoteViews("com.app.meupedido", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, description)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.ic_notification)

        return remoteView
    }

    private fun generateNotification(title: String, description: String) {

        val number = title.substring(11,18)
        val order: Unit
        val archived: Archived

        insertArchivedToDatabase(number)
        removeOrderToDatabase(number)


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

        builder = builder.setContent(getRemoteView(title, description))

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
        val archived = Archived(
            numberOrder,
            "Pronto",
            "data",
            "Spoleto",
            "SPL"
        )
        mArchivedViewModel.addArchived(archived)
    }

    private fun removeOrderToDatabase(numberOrder: String) {
        val order = Order(
            numberOrder,
            "Em Execução",
            "data",
            "Spoleto",
            "SPL"
        )
        mOrderViewModel.deleteOrder(order)
        Firebase.messaging.unsubscribeFromTopic(numberOrder)
    }
}