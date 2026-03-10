package com.waffiq.bazz_movies.core.movie.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse

object TestVariables {

  fun createSampleMediaItemResponse(
    id: Int = 1,
    name: String = "Test Name",
  ): MediaResponseItem = MediaResponseItem(id = id, name = name)

  fun createSamplePagingData(
    vararg items: MediaResponseItem,
  ): PagingData<MediaResponseItem> = PagingData.from(items.toList())

  val mediaStateResponse = MediaStateResponse(
    id = 8888,
    favorite = false,
    ratedResponse = RatedResponse.Value(9.0),
    watchlist = true
  )


  val postFavoriteWatchlistResponseSuccess = PostFavoriteWatchlistResponse(
    statusCode = 201,
    statusMessage = "Success"
  )

  val favoriteParams = FavoriteParams(
    mediaType = "movie",
    mediaId = 99999,
    favorite = false
  )

  val watchlistParams = WatchlistParams(
    mediaType = "tv",
    mediaId = 4444,
    watchlist = true
  )

  val postMovieResponseSuccess = PostResponse(
    success = true,
    statusCode = 201,
    statusMessage = "Success Rating Movie"
  )

  val postTvRateResponseSuccess = postMovieResponseSuccess.copy(
    statusMessage = "Success Rating Tv"
  )

  const val MOVIE_ID = 1001
  val movieMediaItem = MediaItem(
    firstAirDate = "",
    overview = "Overview",
    originalLanguage = "",
    listGenreIds = listOf(),
    posterPath = "Poster",
    backdropPath = "Backdrop",
    mediaType = MOVIE_MEDIA_TYPE,
    originalName = "original name",
    popularity = 12345.0,
    voteAverage = 9.0f,
    name = "name",
    id = 1234,
    adult = false,
    voteCount = 999,
    originalTitle = "original title",
    video = false,
    title = "title",
    releaseDate = "release date",
    originCountry = listOf()
  )

  val tvMediaItem = movieMediaItem.copy(mediaType = TV_MEDIA_TYPE)
}
