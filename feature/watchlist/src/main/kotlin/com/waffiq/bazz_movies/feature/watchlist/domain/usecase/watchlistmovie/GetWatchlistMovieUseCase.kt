package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetWatchlistMovieUseCase {
  fun getWatchlistMovies(sortBy: String): Flow<PagingData<MediaItem>>
}
