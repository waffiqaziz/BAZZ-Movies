package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteTvInteractor @Inject constructor(
  private val getFavoriteTvRepository: IMoviesRepository
) : GetFavoriteTvUseCase {
  override fun getPagingFavoriteTv(sessionId: String): Flow<androidx.paging.PagingData<ResultItem>> =
    getFavoriteTvRepository.getPagingFavoriteTv(sessionId)
}
