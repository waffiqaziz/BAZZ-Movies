package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel

/**
 * Test data for the [SharedDBViewModel] tests.
 *
 * This class provides a set of predefined [Favorite] objects to be used in unit tests.
 * It includes lists of favorite TV shows, movies, and watchlist for both TV shows and movies.
 */
class TestData {

  // base test data
  val baseFavorite = Favorite(
    id = 3,
    mediaId = 3003,
    mediaType = "movie",
    genre = "Sci-Fi",
    backDrop = "backdrop_url",
    poster = "poster_url",
    overview = "overview",
    title = "Title",
    releaseDate = "2025-03-03",
    popularity = 8.9,
    rating = 4.3f,
    isFavorite = true,
    isWatchlist = false
  )

  val favoriteTvList = listOf(
    baseFavorite.copy(id = 1, mediaId = 101, mediaType = "tv", title = "TV Show 1"),
    baseFavorite.copy(id = 2, mediaId = 102, mediaType = "tv", title = "TV Show 2")
  )

  val favoriteMoviesList = listOf(
    baseFavorite.copy(id = 3, mediaId = 201, mediaType = "movie", title = "Movie 1"),
    baseFavorite.copy(id = 4, mediaId = 202, mediaType = "movie", title = "Movie 2")
  )

  val watchlistTvList = listOf(
    baseFavorite.copy(
      id = 5,
      mediaId = 301,
      mediaType = "tv",
      title = "TV Show 3",
      isWatchlist = true,
      isFavorite = false
    ),
    baseFavorite.copy(
      id = 6,
      mediaId = 302,
      mediaType = "tv",
      title = "TV Show 4",
      isWatchlist = true,
      isFavorite = false
    )
  )

  val watchlistMovieList = listOf(
    baseFavorite.copy(
      id = 7,
      mediaId = 401,
      mediaType = "movie",
      title = "Movie 3",
      isWatchlist = true,
      isFavorite = false
    ),
    baseFavorite.copy(
      id = 8,
      mediaId = 402,
      mediaType = "movie",
      title = "Movie 4",
      isWatchlist = true,
      isFavorite = false
    )
  )
}
