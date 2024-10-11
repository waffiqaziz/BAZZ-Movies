package com.waffiq.bazz_movies.core.domain.usecase.get_watchlist

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistMovieInteractor @Inject constructor(
  private val getWatchlistMovieRepository: IMoviesRepository
) : GetWatchlistMovieUseCase {
  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    getWatchlistMovieRepository.getPagingWatchlistMovies(sessionId)
}
