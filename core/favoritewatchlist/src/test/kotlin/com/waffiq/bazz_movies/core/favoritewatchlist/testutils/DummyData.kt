package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_NAME
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_ORIGINAL_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.MOVIE_TITLE
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.Constants.TEST_DATE
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem

object DummyData {

  const val INA_MOVIE_TITLE = "Indonesian Movie"
  const val INA_MOVIE_TITLE2 = "Indonesian Movie 2"
  val favorite = Favorite(
    id = 1,
    mediaId = 1,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop",
    poster = "poster",
    overview = "overview",
    title = INA_MOVIE_TITLE,
    releaseDate = "1979-04-04",
    popularity = 214.0,
    rating = 9.0f,
    isFavorite = true,
    isWatchlist = false,
    lastUpdated = 333,
  )

  val movieData = MediaItem(
    mediaType = "movie",
    name = "Test Movie Name",
    title = MOVIE_TITLE,
    originalTitle = MOVIE_ORIGINAL_TITLE,
    originalName = MOVIE_ORIGINAL_NAME,
    firstAirDate = TEST_DATE,
    releaseDate = TEST_DATE,
    listGenreIds = listOf(12),
    voteAverage = 10f,
    posterPath = "posterPath.jpg",
  )

  val favoriteTvList = listOf(
    favorite.copy(id = 1, mediaId = 101, mediaType = "tv", title = "TV Show 1"),
    favorite.copy(id = 2, mediaId = 102, mediaType = "tv", title = "TV Show 2"),
  )

  val favoriteMoviesList = listOf(
    favorite.copy(id = 3, mediaId = 201, mediaType = "movie", title = "Movie 1"),
    favorite.copy(id = 4, mediaId = 202, mediaType = "movie", title = "Movie 2"),
  )

  val watchlistTvList = listOf(
    favorite.copy(
      id = 5,
      mediaId = 301,
      mediaType = "tv",
      title = "TV Show 3",
      isWatchlist = true,
      isFavorite = false,
    ),
    favorite.copy(
      id = 6,
      mediaId = 302,
      mediaType = "tv",
      title = "TV Show 4",
      isWatchlist = true,
      isFavorite = false,
    ),
  )

  val watchlistMovieList = listOf(
    favorite.copy(
      id = 7,
      mediaId = 401,
      mediaType = "movie",
      title = "Movie 3",
      isWatchlist = true,
      isFavorite = false,
    ),
    favorite.copy(
      id = 8,
      mediaId = 402,
      mediaType = "movie",
      title = "Movie 4",
      isWatchlist = true,
      isFavorite = false,
    ),
  )
}
