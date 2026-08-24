package com.waffiq.bazz_movies.navigation.utils

import com.waffiq.bazz_movies.core.common.MediaType.Companion.fromValue
import com.waffiq.bazz_movies.navigation.testutils.DummyData.mediaArgs
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

  @Test
  fun toMediaItem_withValidValue_returnsCorrectly() {
    val result = mediaArgs.toMediaItem()
    assertEquals(result.id, mediaArgs.id)
    assertEquals(fromValue(result.mediaType), mediaArgs.mediaType)
    assertEquals(result.name, mediaArgs.name)
    assertEquals(result.title, mediaArgs.title)
    assertEquals(result.originalTitle, mediaArgs.originalTitle)
    assertEquals(result.originalName, mediaArgs.originalName)
    assertEquals(result.overview, mediaArgs.overview)
    assertEquals(result.originalLanguage, mediaArgs.originalLanguage)
    assertEquals(result.listGenreIds, mediaArgs.listGenreIds)
    assertEquals(result.posterPath, mediaArgs.posterPath)
    assertEquals(result.backdropPath, mediaArgs.backdropPath)
    assertEquals(result.firstAirDate, mediaArgs.firstAirDate)
    assertEquals(result.releaseDate, mediaArgs.releaseDate)
    assertEquals(result.video, mediaArgs.video)
  }

  @Test
  fun toMediaArgs_withValidValue_returnsCorrectly() {
    val result = mediaArgs.toMediaItem().toMediaArgs()
    assertEquals(result.id, mediaArgs.id)
    assertEquals(result.mediaType, mediaArgs.mediaType)
    assertEquals(result.name, mediaArgs.name)
    assertEquals(result.title, mediaArgs.title)
    assertEquals(result.originalTitle, mediaArgs.originalTitle)
    assertEquals(result.originalName, mediaArgs.originalName)
    assertEquals(result.overview, mediaArgs.overview)
    assertEquals(result.originalLanguage, mediaArgs.originalLanguage)
    assertEquals(result.listGenreIds, mediaArgs.listGenreIds)
    assertEquals(result.posterPath, mediaArgs.posterPath)
    assertEquals(result.backdropPath, mediaArgs.backdropPath)
    assertEquals(result.firstAirDate, mediaArgs.firstAirDate)
    assertEquals(result.releaseDate, mediaArgs.releaseDate)
    assertEquals(result.video, mediaArgs.video)
  }
}
