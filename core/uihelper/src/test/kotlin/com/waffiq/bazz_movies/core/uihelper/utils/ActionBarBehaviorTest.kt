package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.transparentStatusBar
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ActionBarBehaviorTest {

  private lateinit var activity: Activity
  private lateinit var window: Window
  private lateinit var appBarLayout: AppBarLayout

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(Activity::class.java).create().get()
    activity.setTheme(Base_Theme_BAZZ_movies)
    window = activity.window

    val themedContext = ContextThemeWrapper(activity, Base_Theme_BAZZ_movies)
    appBarLayout = AppBarLayout(themedContext)
  }

  @Test
  @Suppress("DEPRECATION")
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun transparentStatusBar_useSystemUIFlagsForApi29AndLower() {
    window.transparentStatusBar()

    // verify that systemUiVisibility has the correct flags
    val expectedVisibilityFlags =
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    assertTrue(window.decorView.systemUiVisibility and expectedVisibilityFlags == expectedVisibilityFlags)

    assertEquals(Color.TRANSPARENT, window.statusBarColor)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.R])
  fun transparentStatusBar_useSetDecorFitsSystemWindowsForApi30AndAbove() {
    window.transparentStatusBar()

    // verify that setDecorFitsSystemWindows(false) was called on the window
    assertFalse(window.decorView.fitsSystemWindows)

    // verify that the statusBarColor is set to transparent
    assertEquals(Color.TRANSPARENT, window.statusBarColor)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q, Build.VERSION_CODES.R])
  fun notCalledTransparentStatusBar_useDefaultColorBlack() {
    assertFalse(window.decorView.fitsSystemWindows)
    assertEquals(Color.BLACK, window.statusBarColor)
  }

  @Test
  fun handleOverHeightAppBar_updateMarginsWithInsets() {
    appBarLayout.layoutParams = ViewGroup.MarginLayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    appBarLayout.handleOverHeightAppBar()

    // simulated system bar insets (top, left, right)
    val systemBarInsets = android.graphics.Insets.of(10, 20, 30, 40)
    val windowInsets = android.view.WindowInsets.Builder()
      .setInsets(android.view.WindowInsets.Type.systemBars(), systemBarInsets)
      .build()

    // trigger the insets listener directly using WindowInsets (not WindowInsetsCompat)
    ViewCompat.setOnApplyWindowInsetsListener(appBarLayout) { v, insets ->
      val params = v.layoutParams as ViewGroup.MarginLayoutParams
      val systemInsets = insets.getInsets(android.view.WindowInsets.Type.systemBars())

      // Update margins based on insets
      params.topMargin = systemInsets.top
      params.leftMargin = systemInsets.left
      params.rightMargin = systemInsets.right

      // Return the consumed insets
      return@setOnApplyWindowInsetsListener WindowInsetsCompat.CONSUMED
    }

    appBarLayout.dispatchApplyWindowInsets(windowInsets)

    val layoutParams = appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
    assertEquals(20, layoutParams.topMargin)
    assertEquals(10, layoutParams.leftMargin)
    assertEquals(30, layoutParams.rightMargin)
  }
}
