package com.waffiq.bazz_movies.cloudmessage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.waffiq.bazz_movies.RoutingActivity
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_monochrome
import com.waffiq.bazz_movies.core.designsystem.R.string.default_notification_channel_id

class FirebaseCloudMessagingService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    Log.d("FirebaseCloudMessagingService", "Refreshed token: $token")
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val title = remoteMessage.notification?.title ?: "New Message"
    val body = remoteMessage.notification?.body ?: "You have a new notification"

    showNotification(title, body)
  }

  private fun showNotification(title: String, message: String) {
    val intent = Intent(this, RoutingActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
      component = ComponentName(packageName, RoutingActivity::class.java.name)
    }

    val pendingIntent = PendingIntent.getActivity(
      this,
      NOTIFICATION_ID,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
    )

    val builder = NotificationCompat.Builder(
      this,
      getString(default_notification_channel_id)
    )
      .setSmallIcon(ic_bazz_monochrome)
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
