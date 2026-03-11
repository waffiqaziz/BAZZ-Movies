package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.os.Build
import java.util.Locale

object Helper {
  fun getCountryDisplayName(region: String): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
      Locale.of("", region).displayCountry
    } else {
      @Suppress("DEPRECATION")
      Locale("", region).displayCountry
    }
}
