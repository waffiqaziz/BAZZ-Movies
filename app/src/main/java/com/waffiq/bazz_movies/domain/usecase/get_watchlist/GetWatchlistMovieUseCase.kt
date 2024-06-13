package com.waffiq.bazz_movies.domain.usecase.get_watchlist

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistMovieUseCase {
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>
}