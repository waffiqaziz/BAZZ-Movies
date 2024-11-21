package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistTvUseCase {
  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
}
