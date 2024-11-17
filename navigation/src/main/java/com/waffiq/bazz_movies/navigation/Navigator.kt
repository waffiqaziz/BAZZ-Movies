package com.waffiq.bazz_movies.navigation

import android.app.Activity
import android.content.Context
import com.waffiq.bazz_movies.core.model.ResultItem
import com.waffiq.bazz_movies.core.model.MovieTvCastItem

interface Navigator {
  fun openPersonDetails(context: Context, cast: MovieTvCastItem)
  fun openDetails(context: Context, resultItem: ResultItem)
  fun openMainActivity(isGuest: Boolean, activity: Activity, context: Context)
  fun openLoginActivity(context: Context)
  fun openAboutActivity(context: Context)
  fun snackbarAnchor(): Int
}
