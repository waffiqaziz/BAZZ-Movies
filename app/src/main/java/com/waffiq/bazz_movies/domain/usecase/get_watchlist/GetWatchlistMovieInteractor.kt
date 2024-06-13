package com.waffiq.bazz_movies.domain.usecase.get_watchlist

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistMovieInteractor(
  private val getWatchlistMovieRepository: IMoviesRepository
) : GetWatchlistMovieUseCase{
  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    getWatchlistMovieRepository.getPagingWatchlistMovies(sessionId)
}