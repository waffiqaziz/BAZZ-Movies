package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.ProductionCountriesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ProductionCompaniesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.SpokenLanguagesResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailMovieResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieMapperTest {

  @Test
  fun toDetailMovie_withValidValues_returnsDetailMovie() {
    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals("en", detailMovie.originalLanguage)
    assertEquals("tt1234567", detailMovie.imdbId)
    assertEquals(false, detailMovie.video)
    assertEquals("Test Movie", detailMovie.title)
    assertEquals("/backdrop.jpg", detailMovie.backdropPath)
    assertEquals(1000000L, detailMovie.revenue)
    assertEquals(1, detailMovie.listGenres?.size)
    assertEquals(8.5, detailMovie.popularity)
    assertEquals(1, detailMovie.id)
    assertEquals(100, detailMovie.voteCount)
    assertEquals(500000, detailMovie.budget)
    assertEquals("Test overview", detailMovie.overview)
    assertEquals("Test Movie Original", detailMovie.originalTitle)
    assertEquals(120, detailMovie.runtime)
    assertEquals("/poster.jpg", detailMovie.posterPath)
    assertEquals("2024-01-01", detailMovie.releaseDate)
    assertEquals(7.5, detailMovie.voteAverage)
    assertEquals("Test tagline", detailMovie.tagline)
    assertEquals(false, detailMovie.adult)
    assertEquals("https://testmovie.com", detailMovie.homepage)
    assertEquals("Released", detailMovie.status)
  }

  @Test
  fun toDetailMovie_withNullValues_returnsDetailMovie() {
    val detailMovieResponse = DetailMovieResponse(
      imdbId = null,
      video = false,
      backdropPath = null,
      listGenresItemResponse = null,
      releaseDatesResponse = null,
      listProductionCountriesItemResponse = null,
      listSpokenLanguagesItemResponse = null,
      listProductionCompaniesItemResponse = null,
      belongsToCollectionResponse = null,
      adult = true,
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals(null, detailMovie.imdbId)
    assertEquals(null, detailMovie.backdropPath)
    assertEquals(null, detailMovie.listGenres)
    assertEquals(null, detailMovie.releaseDates)
    assertEquals(null, detailMovie.belongsToCollection)
  }

  @Test
  fun toDetailMovie_withEmptyLists_returnsDetailMovie() {
    val detailMovieResponse = DetailMovieResponse(
      listGenresItemResponse = emptyList(),
      releaseDatesResponse = ReleaseDatesResponse(
        listReleaseDatesResponseItem = emptyList()
      ),
      listProductionCountriesItemResponse = emptyList(),
      listSpokenLanguagesItemResponse = emptyList(),
      listProductionCompaniesItemResponse = emptyList(),
      belongsToCollectionResponse = BelongsToCollectionResponse(
        backdropPath = null,
        name = "Collection",
        id = 1,
        posterPath = null
      ),
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals(emptyList<GenresItem>(), detailMovie.listGenres)
    assertEquals(emptyList<ReleaseDatesItem>(), detailMovie.releaseDates?.listReleaseDatesItem)
    assertEquals(emptyList<ProductionCountriesItem>(), detailMovie.listProductionCountriesItem)
    assertEquals(emptyList<SpokenLanguagesItem>(), detailMovie.listSpokenLanguagesItem)
    assertEquals(emptyList<ProductionCompaniesItem>(), detailMovie.listProductionCompaniesItem)
    assertEquals(null, detailMovie.belongsToCollection?.backdropPath)
    assertEquals(null, detailMovie.belongsToCollection?.posterPath)
  }

  @Test
  fun toDetailMovie_withNullItemsInList_returnsDetailMovieWithEmptyGenres() {
    val detailMovieResponse = DetailMovieResponse(
      video = false,
      revenue = 1000000,
      listGenresItemResponse = listOf(null, null),
      releaseDatesResponse = null,
      listProductionCountriesItemResponse = listOf(null),
      listSpokenLanguagesItemResponse = listOf(null),
      listProductionCompaniesItemResponse = listOf(null),
      belongsToCollectionResponse = null,
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals(2, detailMovie.listGenres?.size)
    assertEquals(null, detailMovie.listGenres?.get(0))
    assertEquals(null, detailMovie.listGenres?.get(1))
    assertEquals(1, detailMovie.listProductionCountriesItem?.size)
    assertEquals(1, detailMovie.listSpokenLanguagesItem?.size)
    assertEquals(1, detailMovie.listProductionCompaniesItem?.size)
  }

  @Test
  fun toDetailMovie_edgeCase_returnsDetailMovie() {
    val detailMovieResponse = detailMovieResponse.copy(
      listGenresItemResponse = listOf(GenresResponseItem(null, null))
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertEquals(null, detailMovie.listGenres?.get(0)?.name)
    assertEquals(null, detailMovie.listGenres?.get(0)?.id)
  }

  @Test
  fun toDetailMovie_withNullMappingResults_returnsDetailMovieWithDefaults() {
    val detailMovieResponse = DetailMovieResponse(
      listGenresItemResponse = listOf(),
      listProductionCountriesItemResponse = listOf(),
      listSpokenLanguagesItemResponse = listOf(),
      listProductionCompaniesItemResponse = listOf(),
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertEquals(emptyList<GenresItem>(), detailMovie.listGenres)
    assertEquals(emptyList<ProductionCountriesItem>(), detailMovie.listProductionCountriesItem)
    assertEquals(emptyList<SpokenLanguagesItem>(), detailMovie.listSpokenLanguagesItem)
    assertEquals(emptyList<ProductionCompaniesItem>(), detailMovie.listProductionCompaniesItem)
  }

  @Test
  fun toDetailMovie_withNullProductionCountriesItem_returnsDetailMovieWithDefaults() {
    val detailMovieResponse = DetailMovieResponse(
      listProductionCountriesItemResponse = listOf(ProductionCountriesResponseItem()),
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertNull(detailMovie.listProductionCountriesItem?.get(0)?.name)
    assertNull(detailMovie.listProductionCountriesItem?.get(0)?.iso6391)
    assertNull(detailMovie.listProductionCountriesItem?.get(0)?.type)
    assertNull(detailMovie.listProductionCountriesItem?.get(0)?.iso31661)
    assertNull(detailMovie.listProductionCountriesItem?.get(0)?.certification)
  }

  @Test
  fun toDetailMovie_withDefaultValue_returnsDetailMovieWithDefaults() {
    val detailMovieResponse = DetailMovieResponse()

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertNull(detailMovie.listGenres)
    assertNull(detailMovie.listProductionCountriesItem)
    assertNull(detailMovie.listSpokenLanguagesItem)
    assertNull(detailMovie.listProductionCompaniesItem)
  }

  @Test
  fun toDetailMovie_withSpokenLanguagesItemThatMapsToNull_returnsDetailMovieWithNullItems() {
    val spokenLanguagesItemResponse = SpokenLanguagesResponseItem()
    val detailMovieResponse = DetailMovieResponse(
      listSpokenLanguagesItemResponse = listOf(spokenLanguagesItemResponse)
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertEquals(1, detailMovie.listSpokenLanguagesItem?.size)
    assertNull(detailMovie.listSpokenLanguagesItem?.get(0)?.name)
    assertNull(detailMovie.listSpokenLanguagesItem?.get(0)?.iso6391)
    assertNull(detailMovie.listSpokenLanguagesItem?.get(0)?.englishName)
  }

  @Test
  fun toDetailMovie_withProductionCompaniesItemThatMapsToNull_returnsDetailMovieWithNullItems() {
    val productionCompaniesItemResponse = ProductionCompaniesResponseItem()
    val detailMovieResponse = DetailMovieResponse(
      listProductionCompaniesItemResponse = listOf(productionCompaniesItemResponse)
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()
    assertEquals(1, detailMovie.listProductionCompaniesItem?.size)
    assertNull(detailMovie.listProductionCompaniesItem?.get(0)?.name)
    assertNull(detailMovie.listProductionCompaniesItem?.get(0)?.id)
    assertNull(detailMovie.listProductionCompaniesItem?.get(0)?.logoPath)
    assertNull(detailMovie.listProductionCompaniesItem?.get(0)?.originCountry)
  }

  @Test
  fun toReleaseDates_withNullListReleaseDatesItem_returnsReleaseDates() {
    val releaseDatesResponse = ReleaseDatesResponse()
    val detailMovieResponse = DetailMovieResponse(
      releaseDatesResponse = releaseDatesResponse
    )

    val detailMovie = detailMovieResponse.toDetailMovie()
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem?.get(0)?.iso31661)
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem?.get(0)?.listReleaseDatesItemValue)
  }

  @Test
  fun toReleaseDates_withNullListReleaseDatesItemValue_returnsReleaseDates() {
    val detailMovieResponse = DetailMovieResponse(
      releaseDatesResponse = ReleaseDatesResponse(
        listReleaseDatesResponseItem = listOf(ReleaseDatesResponseItem())
      )
    )

    val detailMovie = detailMovieResponse.toDetailMovie()
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem?.get(0)?.listReleaseDatesItemValue)
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem?.get(0)?.iso31661)
  }

  @Test
  fun toReleaseDates_withNullListReleaseDatesResponseItem_returnsReleaseDates() {
    val detailMovieResponse = DetailMovieResponse(
      releaseDatesResponse = ReleaseDatesResponse(
        listReleaseDatesResponseItem = null
      )
    )

    val detailMovie = detailMovieResponse.toDetailMovie()
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem)
    assertNull(detailMovie.releaseDates?.listReleaseDatesItem)

    val detailMovieResponseNull = DetailMovieResponse(
      releaseDatesResponse = ReleaseDatesResponse(
        listReleaseDatesResponseItem = listOf(null)
      )
    )

    val detailMovieNull = detailMovieResponseNull.toDetailMovie()
    assertEquals(listOf(null), detailMovieNull.releaseDates?.listReleaseDatesItem)
  }
}
