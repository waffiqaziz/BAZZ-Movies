package com.waffiq.bazz_movies.core.uihelper.testutils

import android.provider.Settings
import org.robolectric.annotation.Implements

@Implements(Settings.Secure::class)
class ShadowSettingsSecure {
  private val secureSettings = mutableMapOf<String, Int>()

  fun setSecureSetting(name: String, value: Int) {
    secureSettings[name] = value
  }
}
