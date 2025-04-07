package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DetailMovieResponseTest {

  @Test
  fun detailMovieResponse_withValidValues_setsPropertiesCorrectly() {
    val detailMovieResponse = detailMovieResponseDump
    assertEquals("en", detailMovieResponse.originalLanguage)
    assertEquals("tt6263850", detailMovieResponse.imdbId)
    assertTrue(detailMovieResponse.video == false)
    assertEquals("Deadpool & Wolverine", detailMovieResponse.title)
    assertEquals("/lD4mhKoiaXpKrtBEjACeWgz7w0O.jpg", detailMovieResponse.backdropPath)
    assertEquals(1338073645L, detailMovieResponse.revenue)
    assertEquals("Action", detailMovieResponse.listGenresItemResponse?.get(0)?.name)
    assertEquals(856.096, detailMovieResponse.popularity)
    assertEquals(
      "AD",
      detailMovieResponse.releaseDatesResponse?.listReleaseDatesItemResponse?.get(0)?.iso31661
    )
    assertEquals(
      "United States of America",
      detailMovieResponse.listProductionCountriesItemResponse?.get(0)?.name
    )
    assertEquals(533535, detailMovieResponse.id)
    assertEquals(6283, detailMovieResponse.voteCount)
    assertEquals(200000000, detailMovieResponse.budget)
    assertEquals(
      """
        A listless Wade Wilson toils away in civilian life with his days as the morally flexible 
        mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade 
        must reluctantly suit-up again with an even more reluctant Wolverine.
      """.trimIndent(),
      detailMovieResponse.overview
    )
    assertEquals("Deadpool & Wolverine", detailMovieResponse.originalTitle)
    assertEquals(128, detailMovieResponse.runtime)
    assertEquals("/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg", detailMovieResponse.posterPath)
    assertEquals("English", detailMovieResponse.listSpokenLanguagesItemResponse?.get(0)?.name)
    assertEquals(
      "Marvel Studios",
      detailMovieResponse.listProductionCompaniesItemResponse?.get(0)?.name
    )
    assertEquals("2024-07-24", detailMovieResponse.releaseDate)
    assertEquals(7.7, detailMovieResponse.voteAverage)
    assertEquals("Deadpool Collection", detailMovieResponse.belongsToCollectionResponse?.name)
    assertEquals("Come together.", detailMovieResponse.tagline)
    assertEquals(false, detailMovieResponse.adult)
    assertEquals(
      "https://www.marvel.com/movies/deadpool-and-wolverine",
      detailMovieResponse.homepage
    )
    assertEquals("Released", detailMovieResponse.status)
  }

  @Test
  fun detailMovieResponse_withDefaultValues_setsPropertiesCorrectly() {
    val detailMovieResponse = DetailMovieResponse()
    assertNull(detailMovieResponse.originalLanguage)
    assertNull(detailMovieResponse.imdbId)
    assertNull(detailMovieResponse.video)
    assertNull(detailMovieResponse.title)
    assertNull(detailMovieResponse.backdropPath)
    assertNull(detailMovieResponse.revenue)
    assertNull(detailMovieResponse.listGenresItemResponse)
    assertNull(detailMovieResponse.popularity)
    assertNull(detailMovieResponse.releaseDatesResponse)
    assertNull(detailMovieResponse.listProductionCountriesItemResponse)
    assertNull(detailMovieResponse.id)
    assertNull(detailMovieResponse.voteCount)
    assertNull(detailMovieResponse.budget)
    assertNull(detailMovieResponse.overview)
    assertNull(detailMovieResponse.originalTitle)
    assertNull(detailMovieResponse.runtime)
    assertNull(detailMovieResponse.posterPath)
    assertNull(detailMovieResponse.listSpokenLanguagesItemResponse)
    assertNull(detailMovieResponse.listProductionCompaniesItemResponse)
    assertNull(detailMovieResponse.releaseDate)
    assertNull(detailMovieResponse.voteAverage)
    assertNull(detailMovieResponse.belongsToCollectionResponse)
    assertNull(detailMovieResponse.tagline)
    assertNull(detailMovieResponse.adult)
    assertNull(detailMovieResponse.homepage)
    assertNull(detailMovieResponse.status)
  }

  @Test
  fun detailMovieResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val detailMovieResponse = DetailMovieResponse(
      title = "Surga Yang Tak Dirindukan",
      id = 2
    )
    assertEquals("Surga Yang Tak Dirindukan", detailMovieResponse.title)
    assertEquals(2, detailMovieResponse.id)
    assertNull(detailMovieResponse.revenue)
    assertNull(detailMovieResponse.releaseDate)
  }
}
