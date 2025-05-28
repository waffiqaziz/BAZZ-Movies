package com.waffiq.bazz_movies.core.uihelper.snackbar

import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event

interface ISnackbar {
  fun showSnackbarWarning(message: String): Snackbar?
  fun showSnackbarWarning(eventMessage: Event<String>): Snackbar?
}
