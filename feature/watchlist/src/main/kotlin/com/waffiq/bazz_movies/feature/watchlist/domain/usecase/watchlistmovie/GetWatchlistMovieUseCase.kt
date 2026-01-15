package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistMovieUseCase {
  fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaItem>>
}
