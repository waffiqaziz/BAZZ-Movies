package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.waffiq.bazz_movies.core.uihelper.testutils.ShadowSettingsSecure
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class GestureHelperTest {
  private val shadowSettingsSecure = ShadowSettingsSecure()
  private lateinit var activity: Activity
  private lateinit var view: View

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(Activity::class.java).get()
    view = View(activity)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R]) // API 30+
  fun addPaddingWhenNavigationEnabled_applyPadding_whenButtonNavigationEnabled_API30OrHigher() {
    mockkConstructor(WindowInsetsControllerCompat::class)
    every { anyConstructed<WindowInsetsControllerCompat>().systemBarsBehavior } returns 0 // Simulate button navigation

    activity.addPaddingWhenNavigationEnable(view)

    // simulate system applying insets
    val insets = WindowInsetsCompat.Builder()
      .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(0, 0, 0, 50))
      .build()
    ViewCompat.dispatchApplyWindowInsets(view, insets)

    assertEquals(50, view.paddingBottom)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R]) // Test on API 30+
  fun addPaddingWhenNavigationEnabled_noPadding_whenGestureNavigationEnabled_API30OrHigher() {
    mockkConstructor(WindowInsetsControllerCompat::class)
    every { anyConstructed<WindowInsetsControllerCompat>().systemBarsBehavior } returns
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    activity.addPaddingWhenNavigationEnable(view)

    // simulate that the system applies insets
    val insets = WindowInsetsCompat.Builder()
      .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(0, 0, 0, 50))
      .build()
    ViewCompat.dispatchApplyWindowInsets(view, insets)

    assertEquals(0, view.paddingBottom)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R])
  fun isButtonNavigationEnabled_callsViewCompatSetOnApplyWindowInsetsListener() {
    mockkConstructor(WindowInsetsControllerCompat::class)
    every { anyConstructed<WindowInsetsControllerCompat>().systemBarsBehavior } returns 1

    mockkStatic(ViewCompat::class)
    activity.addPaddingWhenNavigationEnable(view)

    verify { ViewCompat.setOnApplyWindowInsetsListener(view, any()) }
    unmockkStatic(ViewCompat::class)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R])
  fun isButtonNavigationNotEnabled_doesNotCallViewCompatSetOnApplyWindowInsetsListener() {
    mockkConstructor(WindowInsetsControllerCompat::class)
    every { anyConstructed<WindowInsetsControllerCompat>().systemBarsBehavior } returns
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    mockkStatic(ViewCompat::class)
    activity.addPaddingWhenNavigationEnable(view)

    verify(exactly = 0) { ViewCompat.setOnApplyWindowInsetsListener(view, any()) }
    unmockkStatic(ViewCompat::class)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun addPaddingWhenNavigationEnabled_applyPadding_whenButtonNavigationEnabled_API29OrLower() {
    // set button navigation enabled
    shadowSettingsSecure.setSecureSetting("secure_gesture_navigation", 0)
    activity.addPaddingWhenNavigationEnable(view)

    // simulate the system dispatching insets
    val insets = WindowInsetsCompat.Builder()
      .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(0, 0, 0, 50))
      .build()
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insetsCompat ->
      val systemBarInsets = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
      view.setPadding(0, 0, 0, systemBarInsets.bottom)
      insetsCompat
    }
    ViewCompat.dispatchApplyWindowInsets(view, insets)

    assertEquals(50, view.paddingBottom)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun addPaddingWhenNavigationEnabled_noPadding_whenGestureNavigationEnabled_API29OrLower() {
    // set gesture navigation enabled
    shadowSettingsSecure.setSecureSetting("secure_gesture_navigation", 1)
    activity.addPaddingWhenNavigationEnable(view)

    // simulate the system dispatching insets
    val insets = WindowInsetsCompat.Builder()
      .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(0, 0, 0, 50))
      .build()
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insetsCompat ->
      // no padding is applied since gesture navigation is enabled
      insetsCompat
    }
    ViewCompat.dispatchApplyWindowInsets(view, insets)

    assertEquals(0, view.paddingBottom)
    assertEquals(0, activity.window.decorView.paddingBottom)
  }
}
