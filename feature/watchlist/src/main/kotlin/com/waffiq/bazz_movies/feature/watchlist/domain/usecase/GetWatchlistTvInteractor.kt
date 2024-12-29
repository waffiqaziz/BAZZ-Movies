package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistTvInteractor @Inject constructor(
  private val watchlistRepository: IWatchlistRepository
) : GetWatchlistTvUseCase {
  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    watchlistRepository.getPagingWatchlistTv(sessionId)
}
