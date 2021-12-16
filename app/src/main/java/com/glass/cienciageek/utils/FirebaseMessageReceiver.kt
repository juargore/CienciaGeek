package com.glass.cienciageek.utils

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.glass.cienciageek.ui.main.MainActivity
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.glass.cienciageek.R
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import java.util.*

class FirebaseMessageReceiver : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is received.
        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body
            )
        }
    }

    /**
     * Method to display the notifications.
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(title: String?, message: String?) {

        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Assign channel ID
        val channelId = "notification_channel"

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Pass the intent to PendingIntent to start the next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.mipmap.ic_launcher_pokecoord_round)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification
        builder = builder.setContent(getCustomDesign(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, "pokecoord_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    /** Function to get the custom Design for the display of notification. */
    private fun getCustomDesign(title: String?, message: String?) = RemoteViews(
        applicationContext.packageName, R.layout.view_notification).apply {
        //setImageViewResource(R.id.icon, R.drawable.ic_mexico_flag)
        setTextViewText(R.id.title, title)
        setTextViewText(R.id.message, message)
        setTextViewText(R.id.hour, getRemainingTime())
    }

    private fun getRemainingTime() =
        DateFormat.format("hh:mm aaa", Calendar.getInstance().time) as String
}