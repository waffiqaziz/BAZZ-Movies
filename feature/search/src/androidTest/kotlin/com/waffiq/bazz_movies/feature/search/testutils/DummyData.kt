package com.waffiq.bazz_movies.feature.search.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem

object DummyData {
  val history1 = SearchHistory(1, "transformers", 100L)
  val history2 = SearchHistory(2, "avatar", 200L)
  val history3 = SearchHistory(3, "dear", 300L)

  val mediaItem = MultiSearchItem(
    id = 1,
    title = "movie title",
    overview = "overview",
  )

  val fakeSearchResult = PagingData.from(
    (1..10).map { i ->
      mediaItem.copy(
        id = i + 1,
        title = "movie title $i",
        voteAverage = (1..5).random().toDouble(),
      )
    },
  )
}
