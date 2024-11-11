package com.waffiq.bazz_movies.core.movie.domain.usecase.get_watchlist

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.model.ResultItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistTvInteractor @Inject constructor(
  private val getWatchlistTVRepository: IMoviesRepository
) : GetWatchlistTvUseCase {
  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> =
    getWatchlistTVRepository.getPagingWatchlistTv(sessionId)
}
