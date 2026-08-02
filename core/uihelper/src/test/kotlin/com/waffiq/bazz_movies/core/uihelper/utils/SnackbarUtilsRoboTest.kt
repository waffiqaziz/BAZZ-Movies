package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Looper
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class SnackbarUtilsRoboTest {

  private lateinit var parentView: FrameLayout
  private lateinit var anchorView: FrameLayout
  private val testMessage = "Test Message"

  @Before
  fun setup() {
    val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
    activity.setTheme(Base_Theme_BAZZ_movies)

    parentView = FrameLayout(activity)
    activity.setContentView(parentView)

    anchorView = FrameLayout(activity)

    shadowOf(Looper.getMainLooper()).idle()
  }

  @Test
  fun snackBarWarning_whenColorResourceIsMissing_returnsNull() {
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } throws Resources.NotFoundException()

    val snackbar2 = snackBarWarning(parentView, anchorView, testMessage)
    assertNull(snackbar2)

    unmockkStatic(ContextCompat::class)
  }
}
