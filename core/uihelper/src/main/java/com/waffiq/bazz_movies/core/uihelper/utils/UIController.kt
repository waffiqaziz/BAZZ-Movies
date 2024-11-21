package com.waffiq.bazz_movies.core.uihelper.utils

import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event

interface UIController {
  fun showSnackbarWarning(message: Event<String>): Snackbar?
  fun showSnackbarWarning(message: String): Snackbar?
}
