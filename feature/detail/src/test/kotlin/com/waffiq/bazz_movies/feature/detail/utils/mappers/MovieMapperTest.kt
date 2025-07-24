package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.GenresResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.BelongsToCollectionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponseItemValue
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates.ReleaseDatesResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDatesItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailMovieResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import org.junit.Assert.assertEquals
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
      originalLanguage = "en",
      imdbId = null,
      video = false,
      title = "Test Movie",
      backdropPath = null,
      revenue = 1000000,
      listGenresItemResponse = null,
      popularity = 8.5,
      releaseDatesResponse = null,
      listProductionCountriesItemResponse = null,
      id = 1,
      voteCount = 100,
      budget = 500000,
      overview = "Test overview",
      originalTitle = "Test Movie Original",
      runtime = 120,
      posterPath = "/poster.jpg",
      listSpokenLanguagesItemResponse = null,
      listProductionCompaniesItemResponse = null,
      releaseDate = "2024-01-01",
      voteAverage = 7.5,
      belongsToCollectionResponse = null,
      tagline = "Test tagline",
      adult = false,
      homepage = "https://testmovie.com",
      status = "Released"
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals("en", detailMovie.originalLanguage)
    assertEquals(null, detailMovie.imdbId)
    assertEquals("Test Movie", detailMovie.title)
    assertEquals(null, detailMovie.backdropPath)
    assertEquals(null, detailMovie.listGenres)
    assertEquals(null, detailMovie.releaseDates)
    assertEquals(null, detailMovie.belongsToCollection)
    assertEquals("/poster.jpg", detailMovie.posterPath)
    assertEquals("https://testmovie.com", detailMovie.homepage)
  }

  @Test
  fun toDetailMovie_withEmptyLists_returnsDetailMovie() {
    val detailMovieResponse = DetailMovieResponse(
      originalLanguage = "en",
      imdbId = "tt1234567",
      video = false,
      title = "Test Movie",
      backdropPath = "/backdrop.jpg",
      revenue = 1000000,
      listGenresItemResponse = emptyList(),
      popularity = 8.5,
      releaseDatesResponse = ReleaseDatesResponse(
        listReleaseDatesItemResponse = emptyList()
      ),
      listProductionCountriesItemResponse = emptyList(),
      id = 1,
      voteCount = 100,
      budget = 500000,
      overview = "Test overview",
      originalTitle = "Test Movie Original",
      runtime = 120,
      posterPath = "/poster.jpg",
      listSpokenLanguagesItemResponse = emptyList(),
      listProductionCompaniesItemResponse = emptyList(),
      releaseDate = "2024-01-01",
      voteAverage = 7.5,
      belongsToCollectionResponse = BelongsToCollectionResponse(
        backdropPath = null,
        name = "Collection",
        id = 1,
        posterPath = null
      ),
      tagline = "Test tagline",
      adult = false,
      homepage = "https://testmovie.com",
      status = "Released"
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals("Test Movie", detailMovie.title)
    assertEquals(emptyList<GenresItem>(), detailMovie.listGenres)
    assertEquals(emptyList<ReleaseDatesItem>(), detailMovie.releaseDates?.listReleaseDatesItem)
    assertEquals(emptyList<ProductionCountriesItem>(), detailMovie.listProductionCountriesItem)
    assertEquals(emptyList<SpokenLanguagesItem>(), detailMovie.listSpokenLanguagesItem)
    assertEquals(emptyList<ProductionCompaniesItem>(), detailMovie.listProductionCompaniesItem)
    assertEquals("Collection", detailMovie.belongsToCollection?.name)
    assertEquals(null, detailMovie.belongsToCollection?.backdropPath)
    assertEquals(null, detailMovie.belongsToCollection?.posterPath)
  }

  @Test
  fun toDetailMovie_withNullItemsInList_returnsDetailMovieWithEmptyGenres() {
    val detailMovieResponse = DetailMovieResponse(
      originalLanguage = "en",
      imdbId = "tt1234567",
      video = false,
      title = "Test Movie",
      backdropPath = "/backdrop.jpg",
      revenue = 1000000,
      listGenresItemResponse = listOf(null, null),
      popularity = 8.5,
      releaseDatesResponse = null,
      listProductionCountriesItemResponse = listOf(null),
      id = 1,
      voteCount = 100,
      budget = 500000,
      overview = "Test overview",
      originalTitle = "Test Movie Original",
      runtime = 120,
      posterPath = "/poster.jpg",
      listSpokenLanguagesItemResponse = listOf(null),
      listProductionCompaniesItemResponse = listOf(null),
      releaseDate = "2024-01-01",
      voteAverage = 7.5,
      belongsToCollectionResponse = null,
      tagline = "Test tagline",
      adult = false,
      homepage = "https://testmovie.com",
      status = "Released"
    )

    val detailMovie: MovieDetail = detailMovieResponse.toDetailMovie()

    assertEquals("Test Movie", detailMovie.title)
    assertEquals(2, detailMovie.listGenres?.size)
    assertEquals(GenresItem(), detailMovie.listGenres?.get(0))
    assertEquals(GenresItem(), detailMovie.listGenres?.get(1))
    assertEquals(1, detailMovie.listProductionCountriesItem?.size)
    assertEquals(1, detailMovie.listSpokenLanguagesItem?.size)
    assertEquals(1, detailMovie.listProductionCompaniesItem?.size)
  }
}
