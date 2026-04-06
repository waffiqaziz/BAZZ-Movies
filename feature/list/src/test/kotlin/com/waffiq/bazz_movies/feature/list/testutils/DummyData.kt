package com.waffiq.bazz_movies.feature.list.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType

object DummyData {

  val movieKeywordsArgs = ListArgs(
    listType = ListType.BY_KEYWORD,
    mediaType = MOVIE_MEDIA_TYPE,
    title = "name keywords",
    genreId = 12345,
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
    releaseDate = "2008-01-20",
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

  val fakeMovieResponsePagingData =
    PagingData.from(
      listOf(
        mediaMovieResponseItem,
        mediaMovieResponseItem2,
      )
    )

  val fakeTvResponsePagingData =
    PagingData.from(
      listOf(
        mediaTvResponseItem,
        mediaTvResponseItem2,
      )
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

  val mediaMovieItem = mediaMovieResponseItem.toMediaItem()
  val mediaMovieItem2 = mediaMovieResponseItem2.toMediaItem()
}
