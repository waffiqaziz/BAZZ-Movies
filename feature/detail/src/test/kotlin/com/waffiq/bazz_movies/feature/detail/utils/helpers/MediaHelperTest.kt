package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_0
import android.view.KeyEvent.KEYCODE_8
import android.view.KeyEvent.KEYCODE_BACK
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.video.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.video.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.extractCrewDisplayNames
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getListOfKeywords
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getScoreFromOMDB
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.isBackReleased
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaHelperTest {

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
  fun detailCrew_withValidCrewList_returnsCorrectNamesAndRoles() {
    val crew = listOf(
      MediaCrewItem(job = "Director", name = "John Doe"),
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Producer", name = "Bob Wilson") // Not in jobToNamesMap
    )
    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Director", "Writer"), displayNames)
    assertEquals(listOf("John Doe", "Jane Smith"), joinedNames)
  }

  @Test
  fun detailCrew_withEmptyNullName_filtersOut() {
    val crew = listOf(
      MediaCrewItem(job = "Writer", name = null),
      MediaCrewItem(job = "Writer", name = ""),
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Writer", name = "Bob Jones")
    )
    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Writer"), displayNames)
    assertEquals(listOf("Jane Smith, Bob Jones"), joinedNames)
  }

  @Test
  fun detailCrew_withEmptyCrewList_returnsEmptyLists() {
    val (displayNames, joinedNames) = extractCrewDisplayNames(emptyList())

    assertEquals(emptyList<String>(), displayNames)
    assertEquals(emptyList<String>(), joinedNames)
  }

  @Test
  fun detailCrew_withMultipleNamesSameJob_returnsJoinedNames() {
    val crew = listOf(
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Writer", name = "John Doe")
    )
    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Writer"), displayNames)
    assertEquals(listOf("Jane Smith, John Doe"), joinedNames)
  }

  @Test
  fun toLink_withOfficialTrailer_returnsTrailerKey() {
    val video = Video(
      results = listOf(
        VideoItem(official = true, type = "Trailer", name = "Trailer", key = "trailer_key"),
        VideoItem(official = false, type = "Teaser", name = "Teaser", key = "teaser_key")
      )
    )
    val result = video.toLink()

    assertEquals("trailer_key", result)
  }

  @Test
  fun toLink_withNoOfficialTrailer_returnsFirstKey() {
    val video = Video(
      results = listOf(
        VideoItem(official = false, type = "Teaser", name = "Teaser", key = "teaser_key"),
        VideoItem(official = false, type = "Clip", name = "Clip", key = "clip_key")
      )
    )
    val result = video.toLink()

    assertEquals("teaser_key", result)
  }

  @Test
  fun toLink_withOfficialButNotTrailer_returnsFallbackKey() {
    val video = Video(
      results = listOf(
        VideoItem(official = true, type = "Teaser", name = "Teaser", key = "teaser_key"),
        VideoItem(official = false, type = "Clip", name = "Clip", key = "clip_key")
      )
    )
    val result = video.toLink()

    assertEquals("teaser_key", result)
  }

  @Test
  fun toLink_withEmptyResults_returnsEmptyString() {
    val video = Video(results = emptyList())
    val result = video.toLink()

    assertEquals("", result)
  }

  @Test
  fun toLink_withNoOfficialTrailer_usesFallback() {
    val video = Video(
      results = listOf(
        VideoItem(name = "1", official = false, type = "Trailer", key = "unofficial_trailer"),
        VideoItem(name = "2", official = true, type = "Teaser", key = "official_teaser"),
        VideoItem(name = "3", official = false, type = "Clip", key = "clip_key")
      )
    )
    val result = video.toLink()

    assertEquals("unofficial_trailer", result)
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
  fun getListOfKeywords_whenListIsNull_returnsNull() {
    val result = getListOfKeywords(null)
    assertNull(result)
  }

  @Test
  fun getListOfKeywords_whenListIsEmpty_returnsEmptyList() {
    val result = getListOfKeywords(emptyList())
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenItemIsNull_returnsEmptyList() {
    val result = getListOfKeywords(listOf(null))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenIdIsNull_returnsEmptyList() {
    val item = MediaKeywordsItem(id = null, name = "action")
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenNameIsNull_returnsEmptyList() {
    val item = MediaKeywordsItem(id = 1, name = null)
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenNameIsEmpty_returnsEmptyList() {
    val item = MediaKeywordsItem(id = 1, name = "")
    val result = getListOfKeywords(listOf(item))
    assertEquals(emptyList<MediaKeywordsItem>(), result)
  }

  @Test
  fun getListOfKeywords_whenIdAndNameAreValid_returnsFilteredList() {
    val item = MediaKeywordsItem(id = 1, name = "action")
    val result = getListOfKeywords(listOf(item))
    assertEquals(listOf(item), result)
  }

  @Test
  fun getListOfKeywords_whenMixedValidAndInvalidItems_returnsOnlyValidItems() {
    val validItem1 = MediaKeywordsItem(id = 1, name = "action")
    val validItem2 = MediaKeywordsItem(id = 2, name = "comedy")
    val nullItem = null
    val nullIdItem = MediaKeywordsItem(id = null, name = "drama")
    val nullNameItem = MediaKeywordsItem(id = 4, name = null)
    val emptyNameItem = MediaKeywordsItem(id = 5, name = "")

    val result = getListOfKeywords(
      listOf(validItem1, nullItem, nullIdItem, nullNameItem, emptyNameItem, validItem2)
    )

    assertEquals(listOf(validItem1, validItem2), result)
  }
}
