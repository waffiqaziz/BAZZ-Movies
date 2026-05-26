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
        ViewGroup.LayoutParams.MATCH_PARENT
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
  }

  @Test
  fun setupWindowInsets_whenInsetsApplied_doesNotSetTopOrBottomMargins() {
    view.setupWindowInsets()

    val insetsType = WindowInsetsCompat.Type.systemBars() or
      WindowInsetsCompat.Type.displayCutout()

    val fakeWindowInsets = WindowInsetsCompat.Builder()
      .setInsets(insetsType, androidx.core.graphics.Insets.of(8, 100, 12, 200))
      .build()

    ViewCompat.dispatchApplyWindowInsets(view, fakeWindowInsets)

    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    assertEquals(0, params.topMargin)
    assertEquals(0, params.bottomMargin)
  }

  @Test
  fun setupWindowInsets_whenInsetsApplied_returnsInsetsUnconsumed() {
    var capturedInsets: WindowInsetsCompat? = null

    // Wrap BEFORE calling setupWindowInsets so we can observe the return value
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
      // Temporarily clear the listener to avoid infinite recursion,
      // then call setupWindowInsets's listener manually
      ViewCompat.setOnApplyWindowInsetsListener(v, null)
      v.setupWindowInsets()
      capturedInsets = ViewCompat.dispatchApplyWindowInsets(v, insets)
      capturedInsets
    }

    val insetsType = WindowInsetsCompat.Type.systemBars() or
      WindowInsetsCompat.Type.displayCutout()

    val fakeWindowInsets = WindowInsetsCompat.Builder()
      .setInsets(insetsType, androidx.core.graphics.Insets.of(16, 24, 16, 24))
      .build()

    ViewCompat.dispatchApplyWindowInsets(view, fakeWindowInsets)

    assertEquals(16, capturedInsets?.getInsets(insetsType)?.left)
    assertEquals(16, capturedInsets?.getInsets(insetsType)?.right)
  }

  @Test
  fun setupWindowInsets_whenInsetsAreZero_setsZeroMargins() {
    view.setupWindowInsets()

    val insetsType = WindowInsetsCompat.Type.systemBars() or
      WindowInsetsCompat.Type.displayCutout()

    val fakeWindowInsets = WindowInsetsCompat.Builder()
      .setInsets(insetsType, androidx.core.graphics.Insets.of(0, 0, 0, 0))
      .build()

    ViewCompat.dispatchApplyWindowInsets(view, fakeWindowInsets)

    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    assertEquals(0, params.leftMargin)
    assertEquals(0, params.rightMargin)
  }
}
