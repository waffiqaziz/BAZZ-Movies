package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistTvUseCase {
  fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaItem>>
}
