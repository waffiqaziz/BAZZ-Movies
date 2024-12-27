package com.waffiq.bazz_movies.feature.watchlist.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchlistMovieInteractor @Inject constructor(
  private val getWatchlistMovieRepository: IMoviesRepository
) : GetWatchlistMovieUseCase {
  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    getWatchlistMovieRepository.getPagingWatchlistMovies(sessionId)
}
