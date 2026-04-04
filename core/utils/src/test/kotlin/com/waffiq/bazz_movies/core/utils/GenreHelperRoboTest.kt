package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenre
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GenreHelperRoboTest {

  private val context: Context = ApplicationProvider.getApplicationContext()

  @Test
  fun getGenre_whenGenreIsNull_returnsFallback() {
    val result = context.getGenre(null)
    assertEquals("N/A", result)
  }

  @Test
  fun getGenre_whenGenreIsAvailable_returnsFallback() {
    val result = context.getGenre(listOf(28,12))
    assertEquals("Action, Adventure", result)
  }
}
