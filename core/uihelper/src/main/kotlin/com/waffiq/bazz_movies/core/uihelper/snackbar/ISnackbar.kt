package com.waffiq.bazz_movies.core.uihelper.snackbar

import com.google.android.material.snackbar.Snackbar

interface ISnackbar {
  fun showSnackbarWarning(message: String): Snackbar?
}
