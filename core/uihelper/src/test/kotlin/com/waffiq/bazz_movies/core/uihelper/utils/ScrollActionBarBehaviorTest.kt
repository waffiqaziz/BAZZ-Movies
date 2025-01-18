package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.scrollActionBarBehavior
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ScrollActionBarBehaviorTest {

  private lateinit var activity: Activity
  private lateinit var context: Context
  private lateinit var window: Window
  private lateinit var appBarLayout: AppBarLayout
  private lateinit var nestedScrollView: NestedScrollView
  private lateinit var childView: View

  @Before
  fun setup() {
    activity = Robolectric.buildActivity(Activity::class.java).create().get()
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    window = activity.window
    appBarLayout = AppBarLayout(context)
    nestedScrollView = NestedScrollView(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        1000 // set a fixed height for the scroll view
      )
    }
    childView = View(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        2000 // make the child view taller than the scroll view
      )
      setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
    }
    nestedScrollView.addView(childView)
    setupNestedTest(nestedScrollView, childView)
  }

  private fun setupNestedTest(nestedScrollView: NestedScrollView, childView: View) {
    // manually measure and layout the NestedScrollView
    nestedScrollView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY), // width: match_parent
      View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY) // height: 1000
    )
    nestedScrollView.layout(0, 0, 1080, 1000)

    // manually measure and layout the child view
    childView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.EXACTLY)
    )
    childView.layout(0, 0, 1080, 2000)
  }

  @Test
  @Config(sdk = [34]) // simulate API 34
  @Suppress("DEPRECATION")
  fun scrollActionBarBehavior_API30_ChangesColors() {
    context.scrollActionBarBehavior(window, appBarLayout, nestedScrollView)
    val maxScrollY = nestedScrollView.getChildAt(0).height - nestedScrollView.height
    assertTrue(maxScrollY > 0)

    // save initial color
    val initialStatusBarColor = window.statusBarColor
    assertNotNull(initialStatusBarColor)
    val initialAppBarColor = (appBarLayout.background as MaterialShapeDrawable)
      .fillColor?.defaultColor
    assertNotNull(initialStatusBarColor)
    assertNotNull(initialAppBarColor)

    // scroll halfway
    nestedScrollView.scrollTo(0, maxScrollY / 2)
    val halfwayStatusBarColor = window.statusBarColor
    val halfwayAppBarColor = (appBarLayout.background as ColorDrawable).color
    assertNotNull(halfwayAppBarColor)
    assertNotNull(halfwayStatusBarColor)

    // scroll to the end
    nestedScrollView.scrollTo(0, maxScrollY)
    val finalStatusBarColor = window.statusBarColor
    val finalAppBarColor = (appBarLayout.background as ColorDrawable).color
    assertNotNull(finalStatusBarColor)
    assertNotNull(finalAppBarColor)

    // assert colors have changed during the scroll
    assertNotEquals(initialStatusBarColor, halfwayStatusBarColor)
    assertNotEquals(initialAppBarColor, halfwayAppBarColor)
    assertNotEquals(halfwayStatusBarColor, finalStatusBarColor)
    assertNotEquals(halfwayAppBarColor, finalAppBarColor)
  }

  // TODO: missing test for Android API 35
}
