package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistMovieUseCase {
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>
}
