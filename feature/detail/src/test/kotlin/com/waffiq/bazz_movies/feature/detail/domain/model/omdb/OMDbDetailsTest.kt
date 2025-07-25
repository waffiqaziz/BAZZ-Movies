package com.waffiq.bazz_movies.feature.detail.domain.model.omdb

import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.oMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ratingsItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OMDbDetailsTest {

  @Test
  fun createOMDbDetails_withDefaultValues_shouldCreateInstance() {
    val item = OMDbDetails()

    assertNull(item.metascore)
    assertNull(item.boxOffice)
    assertNull(item.website)
    assertNull(item.imdbRating)
    assertNull(item.imdbVotes)
    assertNull(item.ratings)
    assertNull(item.runtime)
    assertNull(item.language)
    assertNull(item.rated)
    assertNull(item.production)
    assertNull(item.released)
    assertNull(item.imdbID)
    assertNull(item.plot)
    assertNull(item.director)
    assertNull(item.title)
    assertNull(item.actors)
    assertNull(item.response)
    assertNull(item.type)
    assertNull(item.awards)
    assertNull(item.dVD)
    assertNull(item.year)
    assertNull(item.poster)
    assertNull(item.country)
    assertNull(item.genre)
    assertNull(item.writer)
  }

  @Test
  fun createOMDbDetails_withAllValues_shouldSetPropertiesCorrectly() {
    val item = oMDbDetails
    assertEquals("85", item.metascore)
    assertEquals("$100M", item.boxOffice)
    assertEquals("example.com", item.website)
    assertEquals("8.5", item.imdbRating)
    assertEquals("100,000", item.imdbVotes)
    assertEquals(ratingsItem, item.ratings)
    assertEquals("120 min", item.runtime)
    assertEquals("English", item.language)
    assertEquals("PG-13", item.rated)
    assertEquals("Studio", item.production)
    assertEquals("2024-01-01", item.released)
    assertEquals("tt1234567", item.imdbID)
    assertEquals("Plot summary", item.plot)
    assertEquals("Director Name", item.director)
    assertEquals("Movie Title", item.title)
    assertEquals("Actor Names", item.actors)
    assertEquals("True", item.response)
    assertEquals("movie", item.type)
    assertEquals("Award Info", item.awards)
    assertEquals("2024-06-01", item.dVD)
    assertEquals("2024", item.year)
    assertEquals("poster.jpg", item.poster)
    assertEquals("USA", item.country)
    assertEquals("Action", item.genre)
    assertEquals("Writer Name", item.writer)
  }
}
