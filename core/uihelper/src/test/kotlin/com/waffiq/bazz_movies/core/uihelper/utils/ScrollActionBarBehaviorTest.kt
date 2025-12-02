package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarUtils.scrollActionBarBehavior
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers

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
    // measure and layout the NestedScrollView
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
  @Config(sdk = [34])
  @Suppress("DEPRECATION")
  fun scrollActionBarBehavior_onApi30_changesTheColors() {
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

  @Test
  @Config(sdk = [34])
  @Suppress("DEPRECATION")
  fun scrollActionBarBehavior_whenMaxScrollNegative_doesNotChangeColors() {
    nestedScrollView.removeAllViews()
    val shorterChild = View(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        500 // shorter than the 1000px scroll view
      )
      setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
    }
    nestedScrollView.addView(shorterChild)

    // force measure and layout
    nestedScrollView.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY)
    )
    nestedScrollView.layout(0, 0, 1080, 1000)

    shorterChild.measure(
      View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY)
    )
    shorterChild.layout(0, 0, 1080, 500)

    // save initial colors
    val initialStatusBarColor = window.statusBarColor
    val initialAppBarColor =
      (appBarLayout.background as? MaterialShapeDrawable)?.fillColor?.defaultColor
        ?: Color.TRANSPARENT

    // apply the behavior
    context.scrollActionBarBehavior(window, appBarLayout, nestedScrollView)

    // verify maxScroll is negative or zero
    val maxScroll = shorterChild.height - nestedScrollView.height
    assertTrue("maxScroll should be negative", maxScroll < 0)

    // attempt to scroll (this should trigger the listener even though it actually not scrolling)
    nestedScrollView.scrollTo(0, 100)

    // verify colors is not changed since percentage should be 0
    val currentStatusBarColor = window.statusBarColor
    val currentAppBarColor = (appBarLayout.background as? ColorDrawable)?.color
      ?: (appBarLayout.background as? MaterialShapeDrawable)?.fillColor?.defaultColor
      ?: Color.TRANSPARENT

    assertEquals(
      "Status bar color should not change when maxScroll <= 0",
      initialStatusBarColor,
      currentStatusBarColor
    )
    assertEquals(
      "AppBar color should not change when maxScroll <= 0",
      initialAppBarColor,
      currentAppBarColor
    )
  }

  @Test
  @Config(sdk = [34])
  fun scrollActionBarBehavior_whenMaxScrollNegative_setsTransparentAppBar() {
    // create test context and mocks
    val spyContext = spyk(context)
    val mockScrollView = mockk<NestedScrollView>(relaxed = true)
    val mockChildView = mockk<View>()

    // set dimensions to ensure maxScroll <= 0
    every { mockScrollView.getChildAt(0) } returns mockChildView
    every { mockScrollView.height } returns 1000
    every { mockChildView.height } returns 800 // Ensures maxScroll is negative

    // capture the scroll listener when it's set
    val listenerSlot = slot<NestedScrollView.OnScrollChangeListener>()
    every { mockScrollView.setOnScrollChangeListener(capture(listenerSlot)) } just Runs

    // create a spy on appBarLayout to track its color changes
    val spyAppBarLayout = spyk(appBarLayout)

    // get the fromColor (transparent) used in the implementation
    val fromColor = ContextCompat.getColor(context, android.R.color.transparent)

    // apply the behavior with mock objects
    spyContext.scrollActionBarBehavior(window, spyAppBarLayout, mockScrollView)

    // trigger the scroll listener with a non-zero scrollY value
    listenerSlot.captured.onScrollChange(mockScrollView, 0, 100, 0, 0)

    // verify maxScroll is negative (or zero)
    val maxScroll = mockChildView.height - mockScrollView.height
    assertTrue("maxScroll should be <= 0", maxScroll <= 0)

    // when maxScroll <= 0, percentage should be 0, so the background is set to `fromColor` (transparent)
    val backgroundDrawable = spyAppBarLayout.background as? ColorDrawable
    assertNotNull("AppBarLayout should have a ColorDrawable background", backgroundDrawable)
    assertEquals(
      "AppBarLayout color should be the fromColor when maxScroll <= 0",
      fromColor,
      backgroundDrawable?.color
    )
  }

  @Test
  @Config(sdk = [35])
  fun scrollActionBarBehavior_onApi35_usesModernApproach() {
    mockkConstructor(WindowInsetsControllerCompat::class)

    every { anyConstructed<WindowInsetsControllerCompat>().isAppearanceLightStatusBars } returns true
    every {
      anyConstructed<WindowInsetsControllerCompat>().isAppearanceLightStatusBars = any()
    } just runs

    context.scrollActionBarBehavior(window, appBarLayout, nestedScrollView)

    val maxScrollY = nestedScrollView.getChildAt(0).height - nestedScrollView.height
    nestedScrollView.scrollTo(0, maxScrollY / 2)

    // expect status bar background view has been added
    val rootView = window.decorView as ViewGroup
    val statusBarBackgroundView = rootView.findViewWithTag<View>("statusBarBackground")
    assertNotNull("Status bar background view should be added on API 35+", statusBarBackgroundView)

    verify { anyConstructed<WindowInsetsControllerCompat>().isAppearanceLightStatusBars = any() }

    // cleanup to avoid test interference
    unmockkConstructor(WindowInsetsControllerCompat::class)
  }

  @Test
  @Config(sdk = [35])
  fun scrollActionBarBehavior_onApi35_removesExistingStatusBarBackground() {
    ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 35)

    // add initial background
    val rootView = window.decorView as ViewGroup
    val existingBackground = View(context).apply {
      tag = "statusBarBackground"
      setBackgroundColor(Color.RED)
    }
    rootView.addView(existingBackground)

    context.scrollActionBarBehavior(window, appBarLayout, nestedScrollView)

    // trigger scrolling to activate the behavior
    nestedScrollView.scrollTo(0, 100)

    // verify only one background view exists (the old one was removed)
    var count = 0
    for (i in 0 until rootView.childCount) {
      val child = rootView.getChildAt(i)
      if (child.tag == "statusBarBackground") {
        count++
      }
    }
    assertEquals("Should only be one status bar background view", 1, count)

    // get the current background view and verify it's not the original red one
    val currentBackground = rootView.findViewWithTag<View>("statusBarBackground")
    val backgroundDrawable = currentBackground.background as ColorDrawable
    assertNotEquals(
      "Background should be replaced with a new color",
      Color.RED,
      backgroundDrawable.color
    )
  }
}
