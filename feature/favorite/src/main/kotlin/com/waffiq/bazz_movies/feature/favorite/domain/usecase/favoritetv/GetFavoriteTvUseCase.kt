package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTvUseCase {
  fun getFavoriteTv(sortBy: String): Flow<PagingData<MediaItem>>
}
