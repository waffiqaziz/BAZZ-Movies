package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Context
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_0
import android.view.KeyEvent.KEYCODE_8
import android.view.KeyEvent.KEYCODE_BACK
import androidx.annotation.PluralsRes
import com.waffiq.bazz_movies.core.designsystem.R.plurals
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.formatRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getEpisodesFormatted
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getOverview
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getScoreFromOMDB
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.isBackReleased
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.showDuration
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MediaHelperTest {

  private val context: Context = mockk()
  private val noOverview = "Theres no overview translated in English"

  private fun mockPlural(
    @PluralsRes resId: Int,
    quantity: Int,
    value: String,
  ) {
    every { context.resources.getQuantityString(resId, quantity, quantity) } returns value
  }

  @Before
  fun setup() {
    every { context.getString(no_overview) } returns noOverview
    mockPlural(plurals.episodes, 1, "1 episode")
    mockPlural(plurals.episodes, 36, "36 episodes")
    mockPlural(plurals.seasons, 1, "1 season")
    mockPlural(plurals.seasons, 3, "3 seasons")
  }

  @Test
  fun convertRuntime_withValidMinutes_returnsCorrectFormat() {
    val result = getTransformDuration(125)
    assertEquals("2h 5m", result)
  }

  @Test
  fun convertRuntime_withZeroMinutes_returnsNull() {
    val result = getTransformDuration(0)
    assertNull(result)
  }

  @Test
  fun convertRuntime_withNullInput_returnsNull() {
    val result = getTransformDuration(null)
    assertNull(result)
  }

  @Test
  fun getTransformTMDBScore_withValidScore_returnsStringScore() {
    val result = getTransformTMDBScore(7.5)
    assertEquals("7.5", result)
  }

  @Test
  fun getTransformTMDBScore_withZeroScore_returnsNull() {
    val result = getTransformTMDBScore(0.0)
    assertNull(result)
  }

  @Test
  fun getTransformTMDBScore_withNullScore_returnsNull() {
    val result = getTransformTMDBScore(null)
    assertNull(result)
  }

  @Test
  fun getScoreFromOMDB_withInvalidScore_returnsFalse() {
    assertFalse(getScoreFromOMDB(null))
    assertFalse(getScoreFromOMDB("N/A"))
  }

  @Test
  fun getScoreFromOMDB_withValidScore_returnsTrue() {
    assertTrue(getScoreFromOMDB("9.5"))
  }

  @Test
  fun isBackReleased_backDown_returnsFalse() {
    assertFalse(isBackReleased(KEYCODE_BACK, ACTION_DOWN))
  }

  @Test
  fun isBackReleased_allCorrect_returnsTrue() {
    assertTrue(isBackReleased(KEYCODE_BACK, ACTION_UP))
  }

  @Test
  fun isBackReleased_unknownPress_returnsFalse() {
    assertFalse(isBackReleased(KEYCODE_0, KEYCODE_8))
  }

  @Test
  fun getOverview_whenOverviewIsAvailable_returnsOverview() {
    assertEquals("data overview", context.getOverview("data overview"))
  }

  @Test
  fun getOverview_whenOverviewIsMissing_returnsNotAvailable() {
    assertEquals(noOverview, context.getOverview(""))
    assertEquals(noOverview, context.getOverview(" "))
    assertEquals(noOverview, context.getOverview(null))
  }

  @Test
  fun formatRating_withValue_returnsCorrectly() {
    assertEquals("10.0", formatRating(10.0))
    assertEquals("10.0", formatRating(10.0f))
    assertEquals("10.0", formatRating(10))
    assertEquals("0.0", formatRating(0))
  }

  @Test
  fun getEpisodesFormatted_mixedValue_returnsValueCorrectly() {
    // when null
    assertEquals("-", context.getEpisodesFormatted(null, null))

    // one of episode/seasons are null
    assertEquals("-", context.getEpisodesFormatted(16, null))
    assertEquals("-", context.getEpisodesFormatted(null, 1))

    // valid episodes singular
    assertEquals("1 episode (1 season)", context.getEpisodesFormatted(1, 1))

    // valid episodes plural
    assertEquals("36 episodes (3 seasons)", context.getEpisodesFormatted(36, 3))
  }

  @Test
  fun showDuration_allCondition_returnsCorrectly() {
    assertFalse(showDuration(null, null))
    assertFalse(showDuration(null, "released"))
    assertFalse(showDuration("1h", null))
    assertFalse(showDuration("", ""))
    assertFalse(showDuration(null, ""))
    assertFalse(showDuration("", null))
    assertFalse(showDuration("", "released"))
    assertFalse(showDuration("1h", ""))
    assertTrue(showDuration("1h", "released"))
  }
}
