package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarBehavior.getStatusBarHeight
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarBehavior.isLightColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
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

  // setupRecyclerViewsWithSnap and setupRecyclerViewsWithSnapGridLayout test
  private val recyclerView = RecyclerView(themedContext)

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q])
  fun justifyTextView_shouldWork_onAndroidQAndAbove() {
    justifyTextView(textView)
    assertEquals(textView.justificationMode, LineBreaker.JUSTIFICATION_MODE_INTER_WORD)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O, Build.VERSION_CODES.P])
  fun justifyTextView_shouldWork_onAndroidOToP() {
    justifyTextView(textView)
    assertEquals(textView.justificationMode, Layout.JUSTIFICATION_MODE_INTER_WORD)
  }

  @Test
  fun setupRecyclerViewsWithSnap_attachesCustomSnapHelper() {
    setupRecyclerViewsWithSnap(listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.onFlingListener is CustomSnapHelper)
  }

  @Test
  fun setupRecyclerViewsWithSnap_doesNotAttachSnapHelperIfAlreadySet() {
    recyclerView.onFlingListener = CustomSnapHelper()

    setupRecyclerViewsWithSnap(listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.onFlingListener is CustomSnapHelper)
  }

  @Test
  fun setupRecyclerViewsWithSnapGridLayout_attachesCustomSnapHelperToGridLayoutManager() {
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.layoutManager is GridLayoutManager)
  }

  @Test
  fun setupRecyclerViewsWithSnapGridLayout_doesNotAttachSnapHelperIfAlreadySet() {
    // set RecyclerView with SnapHelper
    recyclerView.onFlingListener = CustomSnapHelper()
    val existingLayoutManager =
      GridLayoutManager(recyclerView.context, 2, GridLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = existingLayoutManager

    // trigger setupRecyclerViewsWithSnapGridLayout
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(recyclerView))

    // snapHelper should not be replaced
    assertNotNull(recyclerView.onFlingListener)

    // ensure layoutManager remains unchanged
    assertSame(existingLayoutManager, recyclerView.layoutManager)
  }

  @Test
  fun animFadeOutLong_hasCorrectDuration() {
    val animation = animFadeOutLong(context)
    assertEquals(animation.duration, DEBOUNCE_VERY_LONG)
  }

  @Test
  fun getStatusBarHeight_returnsZeroWhenInsetsAreNull() {
    mockkStatic(ViewCompat::class)

    val mockDecorView = mockk<View>()
    every { ViewCompat.getRootWindowInsets(mockDecorView) } returns null

    val window = mockk<Window> {
      every { decorView } returns mockDecorView
    }

    assertEquals(0, window.getStatusBarHeight())
  }

  @Test
  fun getStatusBarHeight_returnsCorrectValue() {
    mockkStatic(ViewCompat::class)

    val mockDecorView = mockk<View>()
    val mockWindowInsets = mockk<WindowInsetsCompat> {
      every { getInsets(WindowInsetsCompat.Type.statusBars()) } returns Insets.of(0, 50, 0, 0)
    }

    every { ViewCompat.getRootWindowInsets(mockDecorView) } returns mockWindowInsets
    val window = mockk<Window> {
      every { decorView } returns mockDecorView
    }

    assertEquals(50, window.getStatusBarHeight())
  }

  @Test
  fun isLightColor_detectsLightAndDarkColorsCorrectly() {
    assertTrue(isLightColor(Color.WHITE))
    assertFalse(isLightColor(Color.BLACK))
    assertTrue(isLightColor(Color.LTGRAY))
    assertFalse(isLightColor(Color.DKGRAY))
  }
}
