package com.waffiq.bazz_movies.core.utils.openurl

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.designsystem.R.string.no_browser_installed
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UriLauncherImpl @Inject constructor(@ApplicationContext private val context: Context) :
  UriLauncher {

  @Suppress("TooGenericExceptionCaught")
  override fun launch(url: String) {
    try {
      val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      Log.e("UriLauncher", e.toString())
      Toast.makeText(context, context.getString(no_browser_installed), Toast.LENGTH_SHORT).show()
    }
  }
}
