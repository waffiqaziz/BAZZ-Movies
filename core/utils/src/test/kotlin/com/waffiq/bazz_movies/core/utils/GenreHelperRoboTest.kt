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
  fun getGenre_whenGenreIsEmpty_returnsFallback() {
    val result = context.getGenre(emptyList())
    assertEquals("N/A", result)
  }

  @Test
  fun getGenre_incorrectGenreId_returnsFallback() {
    val result = context.getGenre(listOf(10020202, 1234))
    assertEquals("N/A", result)
  }

  @Test
  fun getGenre_someIncorrectGenreId_returnsFallback() {
    val result = context.getGenre(listOf(10020202, 28))
    assertEquals("Action", result)
  }

  @Test
  fun getGenre_whenGenreIsAvailable_returnsFallback() {
    val result = context.getGenre(listOf(28, 12))
    assertEquals("Action, Adventure", result)
  }
}
