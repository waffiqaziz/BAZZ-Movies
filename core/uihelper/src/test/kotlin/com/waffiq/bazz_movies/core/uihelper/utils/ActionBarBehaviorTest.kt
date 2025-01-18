package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

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
