package com.waffiq.bazz_movies.feature.list.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem

object DummyData {

  val mediaItem = MediaItem(
    id = 1,
    title = "title",
    overview = "overview",
    posterPath = "/poster1.jpg",
    backdropPath = "/backdrop1.jpg",
    mediaType = "movie",
    voteAverage = 8.8f,
    releaseDate = "2016-07-16"
  )

  val fakePagingMediaItem =
    PagingData.from(
      listOf(
        mediaItem,
        mediaItem.copy(id = 2),
      )
    )
}
