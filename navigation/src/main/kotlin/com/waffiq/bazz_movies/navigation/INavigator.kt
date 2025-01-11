package com.waffiq.bazz_movies.navigation

import android.content.Context
import com.waffiq.bazz_movies.core.domain.MovieTvCastItem
import com.waffiq.bazz_movies.core.domain.ResultItem

interface INavigator {
  fun openPersonDetails(context: Context, cast: MovieTvCastItem)
  fun openDetails(context: Context, resultItem: ResultItem)
  fun openMainActivity(context: Context)
  fun openLoginActivity(context: Context)
  fun openAboutActivity(context: Context)
  fun snackbarAnchor(): Int
}
