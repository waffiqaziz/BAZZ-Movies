package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.animFadeOutLong
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
  fun setupRecyclerViewsWithSnapGridLayout_attachesCustomSnapHelperToGridLayoutManager() {
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(recyclerView))

    assertNotNull(recyclerView.onFlingListener)
    assertTrue(recyclerView.layoutManager is GridLayoutManager)
  }

  @Test
  fun animFadeOutLong_hasCorrectDuration() {
    val animation = animFadeOutLong(context)
    assertEquals(animation.duration, DEBOUNCE_VERY_LONG)
  }
}
