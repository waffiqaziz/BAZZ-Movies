package com.waffiq.bazz_movies

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.waffiq.bazz_movies.core.designsystem.R.string.default_notification_channel_id
import com.waffiq.bazz_movies.core.designsystem.R.string.notification_channel_name
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    createNotificationChannels()
  }

  private fun createNotificationChannels() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val firebaseChannel = NotificationChannel(
        ContextCompat.getString(this, default_notification_channel_id),
        ContextCompat.getString(this, notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
      )

      val manager = getSystemService(NotificationManager::class.java)
      manager.createNotificationChannel(firebaseChannel)
    }
  }
}
