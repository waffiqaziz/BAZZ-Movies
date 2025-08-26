package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem

/**
 * DataDump object provides test data for unit tests in the favorite feature module.
 * It includes constants, media items, user data, and outcomes for testing purposes.
 */
object DataDump {

  const val SESSION_ID = "test_session_id"
  const val USER_ID = 123
  const val ERROR_MESSAGE = "Test error message"
  const val TV_ID = 11111
  const val MOVIE_ID = 22222

  val outcomeError = Outcome.Error(ERROR_MESSAGE)
  val outcomeLoading = Outcome.Loading
  fun  <T>outcomeSuccess(data: T) = Outcome.Success(data)

  val tvStateNotWatchlist =  MediaState(
    id = TV_ID,
    watchlist = false,
    favorite = false,
    rated = Rated.Unrated
  )
  val tvStateInWatchlist = tvStateNotWatchlist.copy(watchlist = true)

  val movieStateNotWatchlist = MediaState(
    id = MOVIE_ID,
    watchlist = false,
    favorite = false,
    rated = Rated.Unrated
  )
  val movieStateInWatchlist = movieStateNotWatchlist.copy(watchlist = true)

  val user = UserModel(
    USER_ID,
    SESSION_ID,
    "username",
    "password",
    "region",
    "token",
    true,
    null,
    null
  )

  val mediaMovieResponseItem = MediaResponseItem(
    id = 1,
    title = "Inception",
    overview = "A mind-bending thriller",
    posterPath = "/poster1.jpg",
    mediaType = "movie",
    voteAverage = 8.8f,
    releaseDate = "2010-07-16"
  )

  val mediaMovieResponseItem2 = MediaResponseItem(
    id = 2,
    title = "The Dark Knight",
    overview = "A gritty superhero film",
    posterPath = "/poster2.jpg",
    mediaType = "movie",
    voteAverage = 9.0f,
    releaseDate = "2008-07-18"
  )

  val mediaTvResponseItem = MediaResponseItem(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher turned methamphetamine producer",
    posterPath = "/poster3.jpg",
    mediaType = "tv",
    voteAverage = 9.5f,
    releaseDate = "2008-01-20"
  )

  val mediaTvResponseItem2 = MediaResponseItem(
    id = 2,
    title = "Game of Thrones",
    overview = "A fantasy drama series based on the novels by George.R.R. Martin",
    posterPath = "/poster4.jpg",
    mediaType = "tv",
    voteAverage = 9.3f,
    releaseDate = "2011-04-17"
  )

  val fakeMovieMediaItemPagingData =
    PagingData.from(
      listOf(
        mediaMovieResponseItem.toMediaItem(),
        mediaMovieResponseItem2.toMediaItem(),
      )
    )

  val fakeTvMediaItemPagingData =
    PagingData.from(
      listOf(
        mediaTvResponseItem.toMediaItem(),
        mediaTvResponseItem2.toMediaItem(),
      )
    )

  val fakeMovieResponsePagingData = PagingData.from(
    listOf(
      mediaMovieResponseItem,
      mediaMovieResponseItem2
    )
  )

  val fakeTvResponsePagingData = PagingData.from(
    listOf(
      mediaTvResponseItem,
      mediaTvResponseItem2
    )
  )
}
