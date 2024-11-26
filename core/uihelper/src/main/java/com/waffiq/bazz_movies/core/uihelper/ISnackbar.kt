package com.waffiq.bazz_movies.core.uihelper

import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event

interface ISnackbar {
  fun showSnackbarWarning(message: Event<String>): Snackbar?
}
