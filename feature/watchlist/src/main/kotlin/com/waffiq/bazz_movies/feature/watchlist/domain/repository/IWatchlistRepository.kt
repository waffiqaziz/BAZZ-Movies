package com.waffiq.bazz_movies.feature.watchlist.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface IWatchlistRepository {
  fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaItem>>
  fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaItem>>
}
