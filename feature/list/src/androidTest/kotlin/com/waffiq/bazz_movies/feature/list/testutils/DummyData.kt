package com.waffiq.bazz_movies.feature.list.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.MediaItem

object DummyData {

  val mediaItem = MediaItem(
    id = 1,
    title = "movie title",
    overview = "overview",
    posterPath = "/poster1.jpg",
    backdropPath = "/backdrop1.jpg",
    mediaType = "movie",
    voteAverage = 8.8f,
    releaseDate = "2016-07-16",
  )

  val fakePagingMediaItem = PagingData.from(
    (1..10).map { i ->
      mediaItem.copy(
        id = i,
        title = "movie title $i",
      )
    },
  )
}
