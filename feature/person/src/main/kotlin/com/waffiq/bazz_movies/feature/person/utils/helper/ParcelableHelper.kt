package com.waffiq.bazz_movies.feature.person.utils.helper

import android.content.Intent
import android.os.Build
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity.Companion.EXTRA_PERSON

object ParcelableHelper {

  fun extractMediaCastItemFromIntent(intent: Intent): MediaCastItem? {
    intent.setExtrasClassLoader(MediaCastItem::class.java.classLoader)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_PERSON, MediaCastItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_PERSON)
    }
  }
}
