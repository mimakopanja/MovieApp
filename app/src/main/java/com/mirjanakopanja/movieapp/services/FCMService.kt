package com.mirjanakopanja.movieapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.extensions.CHANNEL_ID
import com.mirjanakopanja.movieapp.extensions.NOTIFICATION_ID
import com.mirjanakopanja.movieapp.extensions.PUSH_MESSAGE_KEY
import com.mirjanakopanja.movieapp.extensions.PUSH_TITLE_KEY

class FCMService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()){
            handleDataMessage(remoteMessageData.toMap())
        }
    }

    private fun handleDataMessage(data : Map<String, String>) {
        val title = data[PUSH_TITLE_KEY]
        val message = data[PUSH_MESSAGE_KEY]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()){
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply{
                setSmallIcon(R.drawable.ic_ticket)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }
}