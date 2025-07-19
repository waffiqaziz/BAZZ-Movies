package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTvUseCase {
  fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaItem>>
}
