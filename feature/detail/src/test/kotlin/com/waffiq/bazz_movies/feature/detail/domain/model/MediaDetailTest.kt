package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaDetailTest {

  private val releaseDateRegion = ReleaseDateRegion(
    regionRelease = "ID",
    releaseDate = "1999-06-07"
  )

  @Test
  fun createMediaDetail_withRequiredParameters_createsInstanceSuccessfully() {
    val detail = MediaDetail(id = 123, releaseDateRegion = releaseDateRegion)

    assertEquals(123, detail.id)
    assertNull(detail.genre)
    assertNull(detail.genreId)
    assertNull(detail.duration)
    assertNull(detail.imdbId)
    assertNull(detail.ageRating)
    assertNull(detail.tmdbScore)
    assertEquals(releaseDateRegion, detail.releaseDateRegion)
  }

  @Test
  fun createMediaDetail_withAllParameters_createsInstanceSuccessfully() {
    val genreIds = listOf(1, 2, 3)
    val detail = MediaDetail(
      id = 456,
      genre = "Action",
      genreId = genreIds,
      duration = "120 min",
      imdbId = "tt1234567",
      ageRating = "PG-13",
      tmdbScore = "8.5",
      releaseDateRegion = releaseDateRegion
    )

    assertEquals(456, detail.id)
    assertEquals("Action", detail.genre)
    assertEquals(genreIds, detail.genreId)
    assertEquals("120 min", detail.duration)
    assertEquals("tt1234567", detail.imdbId)
    assertEquals("PG-13", detail.ageRating)
    assertEquals("8.5", detail.tmdbScore)
    assertEquals(releaseDateRegion, detail.releaseDateRegion)
  }

  @Test
  fun createMediaDetail_withEmptyGenreList_createsInstanceSuccessfully() {
    val detail = MediaDetail(
      id = 789,
      genreId = emptyList(),
      releaseDateRegion = releaseDateRegion
    )

    assertEquals(789, detail.id)
    assertTrue(detail.genreId!!.isEmpty())
    assertEquals(releaseDateRegion, detail.releaseDateRegion)
  }

  @Test
  fun createMediaDetail_withExplicitNulls_createsInstanceSuccessfully() {
    val detail = MediaDetail(
      id = 999,
      genre = null,
      genreId = null,
      duration = null,
      imdbId = null,
      ageRating = null,
      tmdbScore = null,
      releaseDateRegion = releaseDateRegion
    )

    assertEquals(999, detail.id)
    assertNull(detail.genre)
    assertNull(detail.genreId)
    assertNull(detail.duration)
    assertNull(detail.imdbId)
    assertNull(detail.ageRating)
    assertNull(detail.tmdbScore)
    assertEquals(releaseDateRegion, detail.releaseDateRegion)
  }
}
