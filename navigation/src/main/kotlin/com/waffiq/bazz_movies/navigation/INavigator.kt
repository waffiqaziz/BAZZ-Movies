package com.waffiq.bazz_movies.navigation

import android.content.Context
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.core.domain.MediaItem

interface INavigator {
  fun openPersonDetails(context: Context, cast: MediaCastItem)
  fun openDetails(context: Context, mediaItem: MediaItem)
  fun openMainActivity(context: Context)
  fun openLoginActivity(context: Context)
  fun openAboutActivity(context: Context)
  fun snackbarAnchor(): Int
}
