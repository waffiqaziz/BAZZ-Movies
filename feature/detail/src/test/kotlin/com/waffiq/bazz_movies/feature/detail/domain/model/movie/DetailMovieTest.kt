package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.BelongsToCollection
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DetailMovieTest {

  @Test
  fun createDetailMovie_withDefaultValues_shouldCreateInstance() {
    val item = MovieDetail()

    assertNull(item.originalLanguage)
    assertNull(item.imdbId)
    assertNull(item.video)
    assertNull(item.title)
    assertNull(item.backdropPath)
    assertNull(item.revenue)
    assertNull(item.listGenres)
    assertNull(item.popularity)
    assertNull(item.releaseDates)
    assertNull(item.listProductionCountriesItem)
    assertNull(item.id)
    assertNull(item.voteCount)
    assertNull(item.budget)
    assertNull(item.overview)
    assertNull(item.originalTitle)
    assertNull(item.runtime)
    assertNull(item.posterPath)
    assertNull(item.listSpokenLanguagesItem)
    assertNull(item.listProductionCompaniesItem)
    assertNull(item.releaseDate)
    assertNull(item.voteAverage)
    assertNull(item.belongsToCollection)
    assertNull(item.tagline)
    assertNull(item.adult)
    assertNull(item.homepage)
    assertNull(item.status)
  }

  @Test
  fun createDetailMovie_withAllValues_shouldSetPropertiesCorrectly() {
    val listGenres = listOf(GenresItem())
    val releaseDates = ReleaseDates()
    val listProductionCountriesItem = listOf(ProductionCountriesItem(), null)
    val listSpokenLanguagesItem = listOf(SpokenLanguagesItem(), null)
    val listProductionCompaniesItem = listOf(ProductionCompaniesItem(), null)
    val belongsToCollection = BelongsToCollection()

    val item = MovieDetail(
      originalLanguage = "en",
      imdbId = "tt1234567",
      video = true,
      title = "Movie Title",
      backdropPath = "/backdrop.jpg",
      revenue = 1000000L,
      listGenres = listGenres,
      popularity = 8.5,
      releaseDates = releaseDates,
      listProductionCountriesItem = listProductionCountriesItem,
      id = 123,
      voteCount = 1000,
      budget = 500000,
      overview = "Movie overview",
      originalTitle = "Original Title",
      runtime = 120,
      posterPath = "/poster.jpg",
      listSpokenLanguagesItem = listSpokenLanguagesItem,
      listProductionCompaniesItem = listProductionCompaniesItem,
      releaseDate = "2024-01-01",
      voteAverage = 7.5,
      belongsToCollection = belongsToCollection,
      tagline = "Tagline",
      adult = false,
      homepage = "https://example.com",
      status = "Released"
    )

    assertEquals("en", item.originalLanguage)
    assertEquals("tt1234567", item.imdbId)
    assertEquals(true, item.video)
    assertEquals("Movie Title", item.title)
    assertEquals("/backdrop.jpg", item.backdropPath)
    assertEquals(1000000L, item.revenue)
    assertEquals(listGenres, item.listGenres)
    assertEquals(8.5, item.popularity)
    assertEquals(releaseDates, item.releaseDates)
    assertEquals(listProductionCountriesItem, item.listProductionCountriesItem)
    assertEquals(123, item.id)
    assertEquals(1000, item.voteCount)
    assertEquals(500000, item.budget)
    assertEquals("Movie overview", item.overview)
    assertEquals("Original Title", item.originalTitle)
    assertEquals(120, item.runtime)
    assertEquals("/poster.jpg", item.posterPath)
    assertEquals(listSpokenLanguagesItem, item.listSpokenLanguagesItem)
    assertEquals(listProductionCompaniesItem, item.listProductionCompaniesItem)
    assertEquals("2024-01-01", item.releaseDate)
    assertEquals(7.5, item.voteAverage)
    assertEquals(belongsToCollection, item.belongsToCollection)
    assertEquals("Tagline", item.tagline)
    assertEquals(false, item.adult)
    assertEquals("https://example.com", item.homepage)
    assertEquals("Released", item.status)
  }
}
