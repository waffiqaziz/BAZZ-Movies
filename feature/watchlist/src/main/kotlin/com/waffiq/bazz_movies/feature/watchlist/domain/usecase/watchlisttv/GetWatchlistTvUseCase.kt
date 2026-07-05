package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistTvUseCase {
  fun getWatchlistTv(sortBy: String): Flow<PagingData<MediaItem>>
}
