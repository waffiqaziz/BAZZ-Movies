package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaDetailHelperTest {

  @Test
  fun convertRuntime_withValidMinutes_returnsCorrectFormat() {
    val result = MediaHelper.getTransformDuration(125)
    assertEquals("2h 5m", result)
  }

  @Test
  fun convertRuntime_withZeroMinutes_returnsNull() {
    val result = MediaHelper.getTransformDuration(0)
    assertNull(result)
  }

  @Test
  fun convertRuntime_withNullInput_returnsNull() {
    val result = MediaHelper.getTransformDuration(null)
    assertNull(result)
  }

  @Test
  fun detailCrew_withValidCrewList_returnsCorrectNamesAndRoles() {
    val crew = listOf(
      MediaCrewItem(job = "Director", name = "John Doe"),
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Producer", name = "Bob Wilson") // Not in jobToNamesMap
    )
    val (displayNames, joinedNames) = MediaHelper.extractCrewDisplayNames(crew)

    assertEquals(listOf("Director", "Writer"), displayNames)
    assertEquals(listOf("John Doe", "Jane Smith"), joinedNames)
  }

  @Test
  fun detailCrew_withEmptyCrewList_returnsEmptyLists() {
    val (displayNames, joinedNames) = MediaHelper.extractCrewDisplayNames(emptyList())

    assertEquals(emptyList<String>(), displayNames)
    assertEquals(emptyList<String>(), joinedNames)
  }

  @Test
  fun detailCrew_withMultipleNamesSameJob_returnsJoinedNames() {
    val crew = listOf(
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Writer", name = "John Doe")
    )
    val (displayNames, joinedNames) = MediaHelper.extractCrewDisplayNames(crew)

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
  fun toLink_withEmptyResults_returnsEmptyString() {
    val video = Video(results = emptyList())
    val result = video.toLink()

    assertEquals("", result)
  }

  @Test
  fun getTransformTMDBScore_withValidScore_returnsStringScore() {
    val result = MediaHelper.getTransformTMDBScore(7.5)
    assertEquals("7.5", result)
  }

  @Test
  fun getTransformTMDBScore_withZeroScore_returnsNull() {
    val result = MediaHelper.getTransformTMDBScore(0.0)
    assertNull(result)
  }

  @Test
  fun getTransformTMDBScore_withNullScore_returnsNull() {
    val result = MediaHelper.getTransformTMDBScore(null)
    assertNull(result)
  }
}
