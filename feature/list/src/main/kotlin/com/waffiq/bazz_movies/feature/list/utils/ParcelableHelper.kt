package com.waffiq.bazz_movies.feature.list.utils

import android.content.Intent
import android.os.BadParcelableException
import android.os.Build
import com.waffiq.bazz_movies.feature.list.ui.ListActivity.Companion.EXTRA_LIST
import com.waffiq.bazz_movies.navigation.ListArgs

object ParcelableHelper {

  fun extractArgsItemFromIntent(intent: Intent): ListArgs? =
    try {
      intent.setExtrasClassLoader(ListArgs::class.java.classLoader)

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_LIST, ListArgs::class.java)
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_LIST)
      }
    } catch (_: BadParcelableException) {
      null
    } catch (_: ClassCastException) {
      null
    }
}
