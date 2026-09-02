package com.waffiq.bazz_movies.navigation

import android.content.Context

interface INavigator {
  fun openPersonDetails(context: Context, args: PersonArgs)
  fun openDetails(context: Context, args: MediaArgs)
  fun openMainActivity(context: Context)
  fun openLoginActivity(context: Context)
  fun openAboutActivity(context: Context)
  fun snackbarAnchor(): Int
  fun openList(context: Context, args: ListArgs)
}
