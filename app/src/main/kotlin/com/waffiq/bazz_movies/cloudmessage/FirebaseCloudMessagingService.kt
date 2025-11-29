package com.waffiq.bazz_movies.cloudmessage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.waffiq.bazz_movies.RoutingActivity
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.core.designsystem.R.string.default_notification_channel_id

class FirebaseCloudMessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val title = remoteMessage.notification?.title ?: "New Message"
    val body = remoteMessage.notification?.body ?: "You have a new notification"

    showNotification(title, body)
  }

  private fun showNotification(title: String, message: String) {
    val intent = Intent(this, RoutingActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val pendingIntent = PendingIntent.getActivity(
      this,
      NOTIFICATION_ID,
      intent,
      PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
      this,
      ContextCompat.getString(this, default_notification_channel_id)
    )
      .setSmallIcon(ic_bazz_logo)
      .setContentTitle(title)
      .setContentText(message)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)

    val notificationManager =
      getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.notify(NOTIFICATION_ID, builder.build())
  }

  companion object {
    private const val NOTIFICATION_ID = 1
  }
}
