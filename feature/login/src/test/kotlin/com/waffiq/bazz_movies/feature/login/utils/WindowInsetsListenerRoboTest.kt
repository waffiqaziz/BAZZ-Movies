package com.waffiq.bazz_movies.feature.login.utils

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import com.waffiq.bazz_movies.feature.login.utils.InsetListener.applyWindowInsets
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class WindowInsetsListenerRoboTest {

  private lateinit var activity: Activity
  private lateinit var view: View
  private lateinit var mockInsets: WindowInsetsCompat
  private lateinit var mockNavBarInsets: Insets

  companion object {
    private const val PADDING_RIGHT = 58
  }

  @Before
  fun setup() {
    activity = Robolectric.buildActivity(Activity::class.java).create().get()
    view = View(activity)
    mockInsets = mock(WindowInsetsCompat::class.java)
    mockNavBarInsets = mock(Insets::class.java)

    `when`(mockInsets.getInsets(WindowInsetsCompat.Type.systemBars()))
      .thenReturn(mockNavBarInsets)
  }

  @Test
  fun applyWindowInsets_whenPortrait_returnsNull() {
    assertNull(activity.applyWindowInsets(view))
  }

  @Test
  fun applyWindowInsets_whenLandscape_returnsNotNull() {
    setOrientation(Configuration.ORIENTATION_LANDSCAPE)
    assertNotNull(activity.applyWindowInsets(view))
  }

  @Test
  fun applyWindowInsets_whenUseButtonNavigation_returnsCorrectPadding() {
    setOrientation(Configuration.ORIENTATION_LANDSCAPE)

    // non-zero right indicates button navigation
    val navBarInsets = Insets.of(0, 0, 48, 0)

    `when`(mockInsets.getInsets(WindowInsetsCompat.Type.systemBars()))
      .thenReturn(navBarInsets)

    // set initial padding
    view.setPadding(8, 16, 0, 24)

    val result = activity.applyWindowInsets(view)
    assertNotNull(result)

    val returnedInsets = result!!.onApplyWindowInsets(view, mockInsets)

    // verify padding was set correctly
    assertEquals(8, view.paddingLeft)
    assertEquals(16, view.paddingTop)
    assertEquals(PADDING_RIGHT, view.paddingRight)
    assertEquals(24, view.paddingBottom)
    assertEquals(mockInsets, returnedInsets)
  }

  @Test
  fun applyWindowInsets_whenGestureNavigation_setsZeroRightPadding() {
    setOrientation(Configuration.ORIENTATION_LANDSCAPE)

    // zero right indicates gesture navigation
    val navBarInsets = Insets.of(0, 0, 0, 0)

    `when`(mockInsets.getInsets(WindowInsetsCompat.Type.systemBars()))
      .thenReturn(navBarInsets)

    // set initial padding with non-zero right
    view.setPadding(8, 16, 999, 24)

    val result = activity.applyWindowInsets(view)

    assertNotNull(result)

    val returnedInsets = result!!.onApplyWindowInsets(view, mockInsets)

    // verify padding was set correctly
    assertEquals(8, view.paddingLeft)
    assertEquals(16, view.paddingTop)
    assertEquals(0, view.paddingRight)
    assertEquals(24, view.paddingBottom)
    assertEquals(mockInsets, returnedInsets)
  }

  @Suppress("SameParameterValue")
  private fun setOrientation(orientation: Int) {
    val config = Configuration(activity.resources.configuration)
    config.orientation = orientation

    // use Robolectric's configuration management
    RuntimeEnvironment.setQualifiers(
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) "land" else "port"
    )

    // recreate activity with new configuration
    val controller = Robolectric.buildActivity(Activity::class.java)
    activity = controller.create().get()
    view = View(activity)
  }
}
