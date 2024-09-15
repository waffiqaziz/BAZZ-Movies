package com.waffiq.bazz_movies.domain.usecase.get_watchlist

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistTvInteractor(
  private val getWatchlistTVRepository: IMoviesRepository
) : GetWatchlistTvUseCase {
  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    getWatchlistTVRepository.getPagingWatchlistTv(sessionId)
}
