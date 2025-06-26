package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.view.ContextThemeWrapper
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
  fun handleOverHeightAppBar_whenCalled_updateMarginsWithInsets() {
    appBarLayout.layoutParams = ViewGroup.MarginLayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    appBarLayout.handleOverHeightAppBar()

    // simulate system bar insets
    val systemBarInsets = android.graphics.Insets.of(10, 20, 30, 40)
    val windowInsets = android.view.WindowInsets.Builder()
      .setInsets(android.view.WindowInsets.Type.systemBars(), systemBarInsets)
      .build()

    // dispatch insets to the view to trigger listener
    // that already set in `handleOverHeightAppBar`
    WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
    appBarLayout.dispatchApplyWindowInsets(windowInsets)

    // verify margins is set correctly
    val layoutParams = appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
    assertEquals(20, layoutParams.topMargin)
    assertEquals(10, layoutParams.leftMargin)
    assertEquals(30, layoutParams.rightMargin)
  }
}
