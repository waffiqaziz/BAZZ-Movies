package com.waffiq.bazz_movies.core.database.testdummy

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.domain.Favorite

object DummyData {
  val favoriteTvEntity = FavoriteEntity(
    id = 12345,
    mediaId = 103,
    mediaType = TV_MEDIA_TYPE,
    genre = "Drama",
    backDrop = "backdrop3",
    poster = "poster3",
    overview = "overview3",
    title = "Show1",
    releaseDate = "2023-01-03",
    popularity = 9.0,
    rating = 4.8f,
    isFavorite = true,
    isWatchlist = false
  )
  val watchlistTvEntity = favoriteTvEntity.copy(isWatchlist = true)

  val favoriteMovieEntity = FavoriteEntity(
    id = 1,
    mediaId = 101,
    mediaType = MOVIE_MEDIA_TYPE,
    genre = "Action",
    backDrop = "backdrop1",
    poster = "poster1",
    overview = "overview1",
    title = "Movie1",
    releaseDate = "2023-01-01",
    popularity = 8.5,
    rating = 4.5f,
    isFavorite = true,
    isWatchlist = false
  )
  val watchlistMovieEntity = favoriteMovieEntity.copy(isWatchlist = true)

  val favoriteMovie = Favorite(
    id = 234,
    mediaId = 101,
    mediaType = MOVIE_MEDIA_TYPE,
    genre = "Action",
    backDrop = "",
    poster = "",
    overview = "",
    title = "Movie1",
    releaseDate = "",
    popularity = 0.0,
    rating = 0f,
    isFavorite = true,
    isWatchlist = false
  )
  val watchlistMovie = favoriteMovie.copy(isWatchlist = true)

  val favoriteTv = favoriteMovie.copy(mediaType = TV_MEDIA_TYPE)

  val watchlistTv = favoriteMovie.copy(mediaType = TV_MEDIA_TYPE, isWatchlist = true)
}
