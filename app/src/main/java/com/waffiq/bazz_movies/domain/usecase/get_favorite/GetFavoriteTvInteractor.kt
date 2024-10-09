package com.waffiq.bazz_movies.domain.usecase.get_favorite

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteTvInteractor @Inject constructor(
  private val getFavoriteTvRepository: IMoviesRepository
) : GetFavoriteTvUseCase {
  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    getFavoriteTvRepository.getPagingFavoriteTv(sessionId)
}
