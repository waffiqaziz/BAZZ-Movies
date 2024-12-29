package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistMovieInteractor @Inject constructor(
  private val watchlistRepository: IWatchlistRepository
) : GetWatchlistMovieUseCase {
  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    watchlistRepository.getPagingWatchlistMovies(sessionId)
}
