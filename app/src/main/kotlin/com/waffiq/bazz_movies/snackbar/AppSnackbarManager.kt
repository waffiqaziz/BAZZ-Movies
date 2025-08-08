package com.waffiq.bazz_movies.snackbar

import android.content.Context
import android.util.Log
import android.view.View
import android.view.WindowManager
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

@Singleton
class AppSnackbarManager @Inject constructor(
  @ActivityContext private val context: Context,
) : ISnackbar {

  private val activity: AppCompatActivity by lazy { context as AppCompatActivity }

  override fun showSnackbarWarning(message: String): Snackbar? {
    return try {
      val rootView = activity.findViewById<View>(android.R.id.content)
      val bottomNav = activity.findViewById<BottomNavigationView?>(bottom_navigation)

      if (!rootView.isAttachedToWindow) return null

      snackBarWarning(rootView, bottomNav, message)?.apply { show() }
    } catch (e: IllegalStateException) {
      Log.e(TAG, "Illegal state when creating snackbar", e)
      null
    } catch (e: WindowManager.BadTokenException) {
      Log.e(TAG, "Invalid window token when creating snackbar", e)
      null
    }
  }

  override fun showSnackbarWarning(eventMessage: Event<String>): Snackbar? {
    return try {
      val rootView = activity.findViewById<View>(android.R.id.content)
      val bottomNav = activity.findViewById<BottomNavigationView?>(bottom_navigation)

      if (!rootView.isAttachedToWindow) return null

      snackBarWarning(rootView, bottomNav, eventMessage)?.apply { show() }
    } catch (e: IllegalStateException) {
      Log.e(TAG, "Illegal state when creating snackbar", e)
      null
    } catch (e: WindowManager.BadTokenException) {
      Log.e(TAG, "Invalid window token when creating snackbar", e)
      null
    }
  }

  companion object {
    private const val TAG = "AppSnackbarManager"
  }
}
