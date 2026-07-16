package com.waffiq.bazz_movies.feature.login.ui.testutils

import android.content.Context
import android.widget.Toast
import com.waffiq.bazz_movies.core.designsystem.R.string.no_browser_installed
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUriLauncher @Inject constructor(@ApplicationContext private val context: Context) :
  UriLauncher {

  val launchedUris = mutableListOf<String>()
  var shouldFail = false

  override fun launch(url: String) {
    if (shouldFail) {
      Toast.makeText(context, context.getString(no_browser_installed), Toast.LENGTH_SHORT).show()
    } else {
      launchedUris.add(url)
    }
  }

  fun reset() {
    launchedUris.clear()
    shouldFail = false
  }
}
