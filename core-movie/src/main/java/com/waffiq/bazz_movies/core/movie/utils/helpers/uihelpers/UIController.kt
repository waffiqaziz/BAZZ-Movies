package com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers

import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.movie.utils.common.Event

interface UIController {
  fun showSnackbarWarning(message: Event<String>): Snackbar?
  fun showSnackbarWarning(message: String): Snackbar?
}