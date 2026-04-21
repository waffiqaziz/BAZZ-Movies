package com.waffiq.bazz_movies.feature.login.utils.openurl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UriLauncherImpl @Inject constructor(@ApplicationContext private val context: Context) :
  UriLauncher {

  @Suppress("TooGenericExceptionCaught")
  override fun launch(url: String): Result<Unit> {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
      if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
        Result.success(Unit)
      } else {
        Result.failure(ActivityNotFoundException("No browser found to handle: $url"))
      }
    } catch (e: Exception) {
      Log.e("UriLauncher", e.toString())
      Result.failure(e)
    }
  }
}
