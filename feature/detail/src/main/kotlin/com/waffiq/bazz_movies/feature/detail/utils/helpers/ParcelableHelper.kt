package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Intent
import android.os.Build
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.ui.MediaDetailActivity.Companion.EXTRA_MOVIE

object ParcelableHelper {

  fun extractMediaItemFromIntent(intent: Intent): MediaItem? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_MOVIE, MediaItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_MOVIE)
    }
  }
}
