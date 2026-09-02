package com.waffiq.bazz_movies.core.uihelper.testutils

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseAdapterTest {

  protected lateinit var context: Context
  protected lateinit var parent: FrameLayout
  protected lateinit var inflater: LayoutInflater

  @Before
  open fun setup() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    parent = FrameLayout(context)
    inflater = LayoutInflater.from(context)
  }
}
