package com.waffiq.bazz_movies.core.domain.usecase.get_favorite

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieInteractor @Inject constructor(
  private val getFavoriteMovieRepository: IMoviesRepository
) : GetFavoriteMovieUseCase {
  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    getFavoriteMovieRepository.getPagingFavoriteMovies(sessionId)
}