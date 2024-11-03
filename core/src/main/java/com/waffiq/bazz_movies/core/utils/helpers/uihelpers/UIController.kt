package com.waffiq.bazz_movies.core.utils.helpers.uihelpers

import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.utils.common.Event

interface UIController {
  fun showSnackbar(message: Event<String>): Snackbar?
}