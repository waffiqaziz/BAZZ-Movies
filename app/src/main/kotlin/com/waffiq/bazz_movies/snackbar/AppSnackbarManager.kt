package com.waffiq.bazz_movies.snackbar

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AppSnackbarManager is a singleton class that manages Snackbar messages in the application.
 * It provides methods to show warning messages using Snackbar, handling the context and view
 * retrieval internally.
 *
 * @property context The application context used to find views and display Snackbar.
 */
@Singleton
class AppSnackbarManager @Inject constructor(@ActivityContext private val context: Context) :
  ISnackbar {

  private val activity: AppCompatActivity by lazy { context as AppCompatActivity }

  override fun showSnackbarWarning(message: String): Snackbar? =
    createAndShowSnackbar { rootView, bottomNav ->
      snackBarWarning(rootView, bottomNav, message)
    }

  override fun showSnackbarWarning(eventMessage: Event<String>): Snackbar? =
    createAndShowSnackbar { rootView, bottomNav ->
      snackBarWarning(rootView, bottomNav, eventMessage)
    }

  /**
   * Common method to handle snackbar creation with proper error handling and validation.
   *
   * @param snackbarFactory Function that creates the snackbar given rootView and bottomNav
   * @return The created and shown Snackbar, or null if creation failed
   */
  private inline fun createAndShowSnackbar(
    snackbarFactory: (rootView: View, bottomNav: View?) -> Snackbar?,
  ): Snackbar? {
    return try {
      val rootView = activity.findViewById<View>(android.R.id.content)
      if (!rootView.isAttachedToWindow) return null

      val bottomNav = activity.findViewById<BottomNavigationView?>(bottom_navigation)
      snackbarFactory(rootView, bottomNav)?.apply { show() }
    } catch (e: Exception) {
      Log.e(TAG, "Error creating snackbar", e)
      null
    }
  }

  companion object {
    private const val TAG = "AppSnackbarManager"
  }
}
