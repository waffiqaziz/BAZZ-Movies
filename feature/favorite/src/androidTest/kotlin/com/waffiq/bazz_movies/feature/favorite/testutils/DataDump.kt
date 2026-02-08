package com.waffiq.bazz_movies.feature.favorite.testutils

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.UserModel

// Placeholder for data dump or test data related utilities
object DataDump {
  val userModel = UserModel(
    userId = 12345678,
    name = "Test Name",
    username = "Test Username",
    password = "",
    region = "id",
    token = "Test Token",
    isLogin = true,
    gravatarHash = "Gravatar Hast",
    tmdbAvatar = "TMDB Avatar"
  )

  val favoriteMovie = Favorite(
    id = 1,
    mediaId = 550,
    mediaType = "movie",
    title = "Fight Club",
    releaseDate = "1999-10-15",
    isFavorite = true,
    isWatchlist = false,
    genre = "Drama",
    backDrop = "backdrop_path",
    poster = "poster_path",
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal",
    popularity = 9.0,
    rating = 8.3f
  )
  val favoriteMovie2 = favoriteMovie.copy(id = 551, title = "Fight Club 2")

  val favoriteTv = Favorite(
    id = 2,
    mediaId = 1399,
    mediaType = "tv",
    title = "Game of Thrones",
    releaseDate = "2011-04-17",
    isFavorite = true,
    isWatchlist = false,
    genre = "Sci-Fi & Fantasy, Drama, Action & Adventure",
    backDrop = "backdrop_path",
    poster = "poster_path",
    overview = "Overview of the TV show",
    popularity = 0.0,
    rating = 6.0f
  )

  val testMediaItem = MediaItem(
    firstAirDate = "2025-07-09",
    overview = "Overview",
    originalLanguage = "en",
    listGenreIds = listOf(28),
    posterPath = "poster.jpg",
    backdropPath = "backdrop.jpg",
    mediaType = MOVIE_MEDIA_TYPE,
    popularity = 354.5509,
    voteAverage = 7.453f,
    name = "name",
    title = "title",
    originalTitle = "original title",
    originalName = "original name",
    id = 1061474,
    adult = false,
    voteCount = 1102,
    video = false,
    releaseDate = "2025-07-09",
    originCountry = null
  )
}
