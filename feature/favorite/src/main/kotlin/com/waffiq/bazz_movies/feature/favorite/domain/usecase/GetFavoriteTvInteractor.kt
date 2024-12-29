package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteTvInteractor @Inject constructor(
  private val favoriteRepository: IFavoriteRepository
) : GetFavoriteTvUseCase {
  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    favoriteRepository.getPagingFavoriteTv(sessionId)
}
