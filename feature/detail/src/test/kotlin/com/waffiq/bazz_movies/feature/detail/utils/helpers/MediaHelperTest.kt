package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Context
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_0
import android.view.KeyEvent.KEYCODE_8
import android.view.KeyEvent.KEYCODE_BACK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_ORIGINAL
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.video.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.video.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.backdropOriginalSource
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.extractCrewDisplayNames
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.formatRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getListOfKeywords
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getOverview
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getScoreFromOMDB
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.isBackReleased
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.isBackdropNotAvailable
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.posterDetailSource
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MediaHelperTest {

  val context: Context = mockk()
  val noOverview = "Theres no overview translated in English"

  @Before
  fun setup() {
    every { context.getString(no_overview) } returns noOverview
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

  @Test
  fun backdropOriginalUrl_whenAllPathAvailable_returnsBackdrop() {
    val data = MediaItem(backdropPath = "backdrop", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_BACKDROP_ORIGINAL + "backdrop", data.backdropOriginalSource)
  }

  @Test
  fun backdropOriginalUrl_whenBackdropPathMissing_returnsCorrectValue() {
    // all null
    val data1 = MediaItem()
    assertEquals(ic_backdrop_error_filled, data1.backdropOriginalSource)

    // all N/A value
    val data2 = MediaItem(backdropPath = "N/A", posterPath = "N/A")
    assertEquals(ic_backdrop_error_filled, data2.backdropOriginalSource)

    // all empty value
    val data3 = MediaItem(backdropPath = "", posterPath = "")
    assertEquals(ic_backdrop_error_filled, data3.backdropOriginalSource)

    // all blank
    val data4 = MediaItem(backdropPath = " ", posterPath = " ")
    assertEquals(ic_backdrop_error_filled, data4.backdropOriginalSource)

    // backdrop null
    val data5 = MediaItem(posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data5.backdropOriginalSource)

    // backdrop empty
    val data6 = MediaItem(backdropPath = "", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data6.backdropOriginalSource)

    // backdrop blank
    val data7 = MediaItem(backdropPath = " ", posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data7.backdropOriginalSource)
  }

  @Test
  fun isBackdropNotAvailable_whenPathIsAvailable_returnsFalse() {
    val data = MediaItem(backdropPath = "backdrop")
    assertFalse(data.isBackdropNotAvailable)
  }

  @Test
  fun isBackdropNotAvailable_whenPathIsNotAvailable_returnsTrue() {
    // backdrop null
    assertTrue(MediaItem(backdropPath = null).isBackdropNotAvailable)

    // backdrop blank
    assertTrue(MediaItem(backdropPath = " ").isBackdropNotAvailable)

    // backdrop empty
    assertTrue(MediaItem(backdropPath = "").isBackdropNotAvailable)

    // backdrop N/A
    assertTrue(MediaItem(backdropPath = "N/A").isBackdropNotAvailable)
  }

  @Test
  fun posterDetailSource_whenPosterPathIsAvailable_returnsPoster() {
    val data = MediaItem(posterPath = "poster")
    assertEquals(TMDB_IMG_LINK_POSTER_W500 + "poster", data.posterDetailSource)
  }

  @Test
  fun posterDetailSource_whenPosterPathIsMissing_returnsDrawable() {
    // null
    assertEquals(ic_poster_error, MediaItem(posterPath = null).posterDetailSource)

    // empty
    assertEquals(ic_poster_error, MediaItem(posterPath = "").posterDetailSource)

    // blank
    assertEquals(ic_poster_error, MediaItem(posterPath = " ").posterDetailSource)

    // N/A
    assertEquals(ic_poster_error, MediaItem(posterPath = "N/A").posterDetailSource)
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
  fun formatRating_withValue_returnsCorrectly(){
    assertEquals("10.0", formatRating(10.0))
    assertEquals("10.0", formatRating(10.0f))
    assertEquals("10.0", formatRating(10))
    assertEquals("0.0", formatRating(0))
  }
}
