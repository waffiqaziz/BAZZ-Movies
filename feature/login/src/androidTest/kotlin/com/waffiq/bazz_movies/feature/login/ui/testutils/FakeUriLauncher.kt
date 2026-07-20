package com.waffiq.bazz_movies.feature.login.ui.testutils

import android.content.Context
import android.widget.Toast
import com.waffiq.bazz_movies.core.designsystem.R.string.no_browser_installed
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Test fake for [UriLauncher] to avoid opening an actual browser during tests.
 * Tracks launched links in memory and allows simulating edge cases.
 */
@Singleton
class FakeUriLauncher @Inject constructor(@ApplicationContext private val context: Context) :
  UriLauncher {

  // stores all URLs the app attempted to open for verification in tests
  val launchedUris = mutableListOf<String>()

  // set true to simulate a device with no web browser installed
  var shouldFail = false

  // hard to test no browser is installed, so this method mirrors that behavior
  override fun launch(url: String) {
    if (shouldFail) {
      Toast.makeText(context, context.getString(no_browser_installed), Toast.LENGTH_SHORT).show()
    } else {
      launchedUris.add(url)
    }
  }

  // clears state between test runs to ensure proper isolation
  fun reset() {
    launchedUris.clear()
    shouldFail = false
  }
}
