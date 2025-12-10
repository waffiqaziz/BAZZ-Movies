package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteTvInteractor @Inject constructor(
  private val favoriteRepository: IFavoriteRepository
) : GetFavoriteTvUseCase {
  override fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaItem>> =
    favoriteRepository.getFavoriteTv(sessionId)
}
