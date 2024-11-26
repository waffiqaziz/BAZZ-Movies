package com.waffiq.bazz_movies.snackbar

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.uihelper.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSnackbar @Inject constructor(
  private val bottomNavigationView: BottomNavigationView
) : ISnackbar {

  override fun showSnackbarWarning(message: Event<String>): Snackbar? {
    return snackBarWarning(bottomNavigationView, bottomNavigationView, message)
  }
}
