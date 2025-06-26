package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.movie

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DetailMovieResponseTest {
  val detailMovieResponse = detailMovieResponseDump

  @Test
  fun detailMovieResponse_withValidValues_setsBasicInfoCorrectly() {
    assertEquals(533535, detailMovieResponse.id)
    assertEquals("Deadpool & Wolverine", detailMovieResponse.title)
    assertEquals("Deadpool & Wolverine", detailMovieResponse.originalTitle)
    assertEquals("English", detailMovieResponse.listSpokenLanguagesItemResponse?.get(0)?.name)
    assertEquals("en", detailMovieResponse.originalLanguage)
    assertEquals("Action", detailMovieResponse.listGenresItemResponse?.get(0)?.name)
    assertEquals(
      "https://www.marvel.com/movies/deadpool-and-wolverine",
      detailMovieResponse.homepage
    )
    assertEquals(false, detailMovieResponse.adult)
  }

  @Test
  fun detailMovieResponse_withValidValues_setsDatesCorrectly() {
    assertEquals("2024-07-24", detailMovieResponse.releaseDate)
    assertEquals("Released", detailMovieResponse.status)
    assertEquals(
      "AD",
      detailMovieResponse.releaseDatesResponse?.listReleaseDatesItemResponse?.get(0)?.iso31661
    )
  }

  @Test
  fun detailMovieResponse_withValidValues_setsTechnicalDetailsCorrectly() {
    assertEquals(856.096, detailMovieResponse.popularity)
    assertEquals(1338073645L, detailMovieResponse.revenue)
    assertEquals(6283, detailMovieResponse.voteCount)
    assertEquals(200000000, detailMovieResponse.budget)
    assertEquals(7.7, detailMovieResponse.voteAverage)
    assertEquals(128, detailMovieResponse.runtime)
    assertEquals("tt6263850", detailMovieResponse.imdbId)
  }

  @Test
  fun detailMovieResponse_withValidValues_setsCompanyAndProductionCorrectly() {
    assertEquals(
      "United States of America",
      detailMovieResponse.listProductionCountriesItemResponse?.get(0)?.name
    )
    assertEquals(
      "Marvel Studios",
      detailMovieResponse.listProductionCompaniesItemResponse?.get(0)?.name
    )
    assertEquals("Deadpool Collection", detailMovieResponse.belongsToCollectionResponse?.name)
  }

  @Test
  fun detailMovieResponse_withValidValues_setsMediaAssetsCorrectly() {
    assertEquals("/lD4mhKoiaXpKrtBEjACeWgz7w0O.jpg", detailMovieResponse.backdropPath)
    assertEquals("/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg", detailMovieResponse.posterPath)
  }

  @Test
  fun detailMovieResponse_withValidValues_setsOthersPropertiesCorrectly() {
    assertTrue(detailMovieResponse.video == false)
    assertEquals(
      """
        A listless Wade Wilson toils away in civilian life with his days as the morally flexible 
        mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade 
        must reluctantly suit-up again with an even more reluctant Wolverine.
      """.trimIndent(),
      detailMovieResponse.overview
    )
    assertEquals("Come together.", detailMovieResponse.tagline)
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
