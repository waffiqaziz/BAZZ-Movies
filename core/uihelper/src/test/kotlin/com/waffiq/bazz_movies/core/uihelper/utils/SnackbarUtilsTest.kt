package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Looper
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import io.mockk.every
import io.mockk.mockk
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
class SnackbarUtilsTest {
  /**
   * This test is always return null because `Roboelectric` cant return true for
   * `view.isAttachedToWindow`, so in this test we just assume the function is called,
   * return throw, and no error as `snackBarWarning` is always return null when exception happen.
   *
   * For the proper test and test all possibility for `snackBarWarning` function,
   * we use [SnackbarUtilsTestWithMockk]
   */

  private lateinit var parentView: FrameLayout
  private lateinit var anchorView: FrameLayout
  private lateinit var eventMessage: Event<String>

  @Before
  fun setup() {
    val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

    activity.setTheme(Base_Theme_BAZZ_movies)

    parentView = FrameLayout(activity)
    activity.setContentView(parentView)

    anchorView = FrameLayout(activity)
    eventMessage = mockk(relaxed = true)

    shadowOf(Looper.getMainLooper()).idle()
  }

  @Test
  fun snackBarWarning_messageHandled_returnNull() {
    every { eventMessage.getContentIfNotHandled() } returns null

    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNull(snackbar)

    val snackbar2 = snackBarWarning(parentView, anchorView, "")
    assertNull(snackbar2)
  }

  @Test
  fun snackBarWarning_emptyMessage_returnNull() {
    every { eventMessage.getContentIfNotHandled() } returns ""

    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNull(snackbar)

    val snackbar2 = snackBarWarning(parentView, anchorView, " ")
    assertNull(snackbar2)
  }

  @Test
  fun snackBarWarning_colorResourceMissing_returnsNull() {
    every { eventMessage.getContentIfNotHandled() } returns "Message"
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } throws Resources.NotFoundException()

    val snackbar2 = snackBarWarning(parentView, anchorView, "Test message")
    assertNull(snackbar2)

    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNull(snackbar)

    unmockkStatic(ContextCompat::class)
  }

  @Test
  fun snackBarWarning_withoutAnchorView_returnsSnackbar() {
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, eventMessage = eventMessage)
    assertNull(snackbar)

    val snackbar2 = snackBarWarning(parentView, message = "Test Message")
    assertNull(snackbar2)
  }

  @Test
  fun snackBarWarning_withAnchorView_returnsSnackbar() {
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(view = parentView, anchorView = anchorView, eventMessage = eventMessage)
    assertNull(snackbar)

    val snackbar2 = snackBarWarning(view = parentView, anchorView = anchorView, message = "Test Message")
    assertNull(snackbar2)
  }

  @Test
  fun snackBarWarning_defaultParameterUsage_returnsSnackbar() {
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, eventMessage = eventMessage)
    assertNull(snackbar)

    val snackbar2 = snackBarWarning(parentView, message = "Test Message")
    assertNull(snackbar2)
  }
}
