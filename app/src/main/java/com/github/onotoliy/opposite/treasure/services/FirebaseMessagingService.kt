package com.github.onotoliy.opposite.treasure.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.github.onotoliy.opposite.treasure.R
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    companion object{
        const val DEFAULT_NOTIFICATION_CHANEL_ID = "channel_id"
        const val DEFAULT_NOTIFICATION_CHANEL_NAME = "channel_name"
    }

    override fun onNewToken(s: String) {
        Log.e("NEW_TOKEN", s)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val channelID = DEFAULT_NOTIFICATION_CHANEL_ID
        val channelName = DEFAULT_NOTIFICATION_CHANEL_NAME
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

            channel.description = ""
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)

            nm.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.getNotificationChannel(channelID).canBypassDnd()
        }

        val builder = NotificationCompat.Builder(this, channelID)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.design_default_color_surface))
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setStyle(NotificationCompat.BigTextStyle()
                .setBigContentTitle(message.notification?.title)
                .bigText(message.notification?.body))
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory
                .decodeResource(applicationContext.resources, R.drawable.ic_baseline_cached_24))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setAutoCancel(true)

        nm.notify(1000, builder.build())
    }
}
