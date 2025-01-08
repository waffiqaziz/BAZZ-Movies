package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class HelpersTest {

  private val activity: Activity = Robolectric.buildActivity(Activity::class.java).create().get()
  private val context: Context = ApplicationProvider.getApplicationContext<Context>().apply {
    setTheme(Base_Theme_BAZZ_movies) // set the theme
  }
  val themedContext = ContextThemeWrapper(activity, Base_Theme_BAZZ_movies)

  // justifyTextView test
  private val textView = TextView(context)

  // scrollActionBarBehavior test
  private val window = activity.window

  // setupRecyclerViewsWithSnap and setupRecyclerViewsWithSnapGridLayout test
  private val recyclerView = RecyclerView(themedContext)

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun `test justifyTextView on Android Q and above`() {
    justifyTextView(textView)
    assertEquals(textView.justificationMode, LineBreaker.JUSTIFICATION_MODE_INTER_WORD)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O, Build.VERSION_CODES.P])
  fun `test justifyTextView on Android O to P`() {
    justifyTextView(textView)
    assertEquals(textView.justificationMode, Layout.JUSTIFICATION_MODE_INTER_WORD)
  }

  @Test
  fun `test scrollActionBarBehavior should change status bar color and appbar color correctly`() {
    // setup nested scroll and app bar
    val appBarLayout = AppBarLayout(context)
    val nestedScrollView = NestedScrollView(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        1000 // Set a fixed height for the scroll view
      )
    }
    val childView = View(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        2000 // Make the child view taller than the scroll view
      )
      setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
    }
    nestedScrollView.addView(childView)
    setupNestedTest(nestedScrollView, childView)

    // calculate max horizontal scroll
    val maxScrollY = nestedScrollView.getChildAt(0).height - nestedScrollView.height
    assertTrue(maxScrollY > 0)

    context.scrollActionBarBehavior(window, appBarLayout, nestedScrollView) // trigger function

    // save initial color
    val initialStatusBarColor = window.statusBarColor
    assertNotNull(initialStatusBarColor)
    val initialAppBarBg = appBarLayout.background as MaterialShapeDrawable
    val initialAppBarColor = initialAppBarBg.fillColor?.defaultColor

    nestedScrollView.scrollTo(0, maxScrollY / 2) // scroll half

    // get halfway color
    val halfwayStatusBarColor = window.statusBarColor
    val halfwayAppBarBg = appBarLayout.background as ColorDrawable
    val halfwayAppBarColor = halfwayAppBarBg.color

    // assert halfway color
    assertNotNull(halfwayStatusBarColor)
    assertNotNull(halfwayAppBarColor)
    assertNotEquals(initialStatusBarColor, halfwayStatusBarColor)
    assertNotEquals(initialAppBarColor, halfwayAppBarColor)

    nestedScrollView.scrollTo(0, maxScrollY) // Scroll to the end

    // save end color
    val updatedStatusBarColor = window.statusBarColor
    val updatedAppBarBg = appBarLayout.background as ColorDrawable
    val updatedAppBarColor = updatedAppBarBg.color

    // assert end color
    assertNotNull(updatedStatusBarColor)
    assertNotNull(updatedAppBarColor)
    assertNotEquals(initialStatusBarColor, updatedStatusBarColor)
    assertNotEquals(halfwayStatusBarColor, updatedStatusBarColor)
    assertNotEquals(initialAppBarColor, updatedAppBarColor)
    assertNotEquals(halfwayAppBarColor, updatedAppBarColor)
  }

  private fun setupNestedTest(nestedScrollView: NestedScrollView, childView: View) {
    // manually measure and layout the NestedScrollView
    nestedScrollView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY), // width: match_parent
      View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY) // height: 1000
    )
    nestedScrollView.layout(0, 0, 1080, 1000)

    // Manually measure and layout the child view
    childView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.EXACTLY)
    )
    childView.layout(0, 0, 1080, 2000)
  }

  @Test
  fun `test setupRecyclerViewsWithSnap attaches CustomSnapHelper`() {
    setupRecyclerViewsWithSnap(listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.onFlingListener is CustomSnapHelper)
  }

  @Test
  fun `test setupRecyclerViewsWithSnapGridLayout attaches CustomSnapHelper to GridLayoutManager`() {
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.layoutManager is GridLayoutManager)
  }

  @Test
  fun `test animFadeOutLong duration`() {
    val animation = animFadeOutLong(context)
    assertEquals(animation.duration, DEBOUNCE_VERY_LONG)
  }
}
