package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistTvInteractor @Inject constructor(
  private val watchlistRepository: IWatchlistRepository,
) : GetWatchlistTvUseCase {
  override fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaItem>> =
    watchlistRepository.getWatchlistTv(sessionId)
}
