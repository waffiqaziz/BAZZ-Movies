package com.waffiq.bazz_movies.feature.more.ui

import android.content.Context
import android.net.Uri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waffiq.bazz_movies.core.designsystem.R.string.no
import com.waffiq.bazz_movies.core.designsystem.R.string.warning
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_restore_will_overwrite
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_guest_mode
import com.waffiq.bazz_movies.core.designsystem.R.string.warning_signOut_logged_user
import com.waffiq.bazz_movies.core.designsystem.R.string.yes
import com.waffiq.bazz_movies.core.designsystem.R.style.ThemeOverlay_App_AlertDialog

class MoreDialogManager(
  private val context: Context,
  private val onSignOutLoggedIn: (String) -> Unit,
  private val onSignOutGuest: () -> Unit,
  private val onRestoreConfirmed: (Uri) -> Unit,
) {
  fun showSignOutLoggedIn(sessionId: String) {
    MaterialAlertDialogBuilder(context, ThemeOverlay_App_AlertDialog)
      .setTitle(context.getString(warning))
      .setMessage(context.getString(warning_signOut_logged_user))
      .setNegativeButton(context.getString(no)) { dialog, _ -> dialog.dismiss() }
      .setPositiveButton(context.getString(yes)) { dialog, _ ->
        onSignOutLoggedIn(sessionId)
        dialog.dismiss()
      }
      .show()
  }

  fun showSignOutGuestMode() {
    MaterialAlertDialogBuilder(context, ThemeOverlay_App_AlertDialog)
      .setTitle(context.getString(warning))
      .setMessage(context.getString(warning_signOut_guest_mode))
      .setNegativeButton(context.getString(no)) { dialog, _ -> dialog.dismiss() }
      .setPositiveButton(context.getString(yes)) { dialog, _ ->
        onSignOutGuest()
        dialog.dismiss()
      }
      .show()
  }

  fun showConfirmRestore(uri: Uri) {
    MaterialAlertDialogBuilder(context, ThemeOverlay_App_AlertDialog)
      .setTitle(context.getString(warning))
      .setMessage(context.getString(warning_restore_will_overwrite))
      .setNegativeButton(context.getString(no)) { dialog, _ -> dialog.dismiss() }
      .setPositiveButton(context.getString(yes)) { dialog, _ ->
        dialog.dismiss()
        onRestoreConfirmed(uri)
      }
      .show()
  }
}
