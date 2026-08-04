package com.waffiq.bazz_movies

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.waffiq.bazz_movies.R.raw.isrgrootx1
import com.waffiq.bazz_movies.core.designsystem.R.string.default_notification_channel_id
import com.waffiq.bazz_movies.core.designsystem.R.string.notification_channel_name
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem
import java.io.InputStream

@HiltAndroidApp
open class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    createNotificationChannels()
    setupGlideForApi23()
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
  private fun setupGlideForApi23() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
      val certificate = resources.openRawResource(isrgrootx1)
        .bufferedReader()
        .readText()
        .decodeCertificatePem()

      val certificates = HandshakeCertificates.Builder()
        .addTrustedCertificate(certificate)
        .addPlatformTrustedCertificates()
        .build()

      val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
        .build()

      Glide.get(this).registry.replace(
        GlideUrl::class.java,
        InputStream::class.java,
        OkHttpUrlLoader.Factory(okHttpClient),
      )
    }
  }
}
