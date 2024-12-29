package com.waffiq.bazz_movies.feature.watchlist.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import kotlinx.coroutines.flow.Flow

interface IWatchlistRepository {

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
}
