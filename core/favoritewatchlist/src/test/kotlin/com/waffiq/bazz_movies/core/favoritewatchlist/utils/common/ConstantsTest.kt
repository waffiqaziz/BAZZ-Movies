package com.waffiq.bazz_movies.core.favoritewatchlist.utils.common

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.common.Constants.tabMoviesTvHeadingArray
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConstantsTest {

  val context: Context = ApplicationProvider.getApplicationContext<Context>().apply {
    setTheme(Base_Theme_BAZZ_movies) // set the theme
  }

  @Test
  fun tabMoviesTvHeadingArray_whenCalled_returnCorrectValue() {
    assertEquals("Movies", context.getString(tabMoviesTvHeadingArray[0]))
    assertEquals("TV Series", context.getString(tabMoviesTvHeadingArray[1]))
  }
}
