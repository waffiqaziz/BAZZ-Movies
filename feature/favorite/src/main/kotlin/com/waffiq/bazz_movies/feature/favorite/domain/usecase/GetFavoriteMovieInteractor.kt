package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieInteractor @Inject constructor(
  private val getFavoriteMovieRepository: IMoviesRepository
) : GetFavoriteMovieUseCase {
  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    getFavoriteMovieRepository.getPagingFavoriteMovies(sessionId)
}
