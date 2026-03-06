package com.waffiq.bazz_movies

import android.app.Application
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.waffiq.bazz_movies.R.raw.isrgrootx1
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem
import java.io.InputStream

@HiltAndroidApp
open class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    setupGlideForApi23()
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
