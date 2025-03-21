package com.waffiq.bazz_movies.feature.search.testutils

import androidx.paging.AsyncPagingDataDiffer
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestDiffCallback
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestListCallback
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import kotlinx.coroutines.Dispatchers

object SearchTestVariables {

  const val QUERY = "transformers"
  val resultsItemSearchResponse = ResultsItemSearchResponse(
    mediaType = TV_MEDIA_TYPE,
    title = "Transformers TV-series",
    id = 12345,
    adult = false,
    voteCount = 2222.0
  )
  val resultsItemSearchResponse2 = ResultsItemSearchResponse(
    mediaType = MOVIE_MEDIA_TYPE,
    title = "Transformers 2",
    id = 333111,
    adult = false,
    voteCount = 3333.0
  )
  val multiSearchResponse = MultiSearchResponse(
    page = 1,
    results = listOf(resultsItemSearchResponse, resultsItemSearchResponse2),
    totalPages = 1,
    totalResults = 2
  )
  val differ = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<ResultsItemSearch>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )
}
