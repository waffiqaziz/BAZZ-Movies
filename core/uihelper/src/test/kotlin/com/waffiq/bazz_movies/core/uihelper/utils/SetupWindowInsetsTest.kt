package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.waffiq.bazz_movies.core.uihelper.utils.InsetHelper.setupWindowInsets
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SetupWindowInsetsTest {

  private lateinit var activity: Activity
  private lateinit var view: View

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(Activity::class.java).create().get()
    view = View(activity).apply {
      layoutParams = ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
      )
    }
    activity.setContentView(view)
  }

  @Test
  fun setupWindowInsets_whenInsetsApplied_setsLeftAndRightMargins() {
    view.setupWindowInsets()

    val insetsType = WindowInsetsCompat.Type.systemBars() or
      WindowInsetsCompat.Type.displayCutout()

    val fakeWindowInsets = WindowInsetsCompat.Builder()
      .setInsets(insetsType, androidx.core.graphics.Insets.of(16, 0, 24, 0))
      .build()

    ViewCompat.dispatchApplyWindowInsets(view, fakeWindowInsets)

    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    assertEquals(16, params.leftMargin)
    assertEquals(24, params.rightMargin)

    // does not set top or bottom margins
    assertEquals(0, params.topMargin)
    assertEquals(0, params.bottomMargin)
  }
}
