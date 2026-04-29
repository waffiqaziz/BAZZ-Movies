package com.waffiq.bazz_movies.feature.login.ui.testutils

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.core.net.toUri
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import javax.inject.Inject

class FakeUriLauncher @Inject constructor() : UriLauncher {
  val launchedUris = mutableListOf<Uri>()
  var shouldFail = false

  override fun launch(url: String): Result<Unit> =
    if (shouldFail) {
      Result.failure(ActivityNotFoundException())
    } else {
      launchedUris.add(url.toUri())
      Result.success(Unit)
    }
}
