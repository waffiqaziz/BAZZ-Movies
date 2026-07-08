package com.waffiq.bazz_movies.feature.favorite.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.models.MediaItem
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
  fun getFavoriteMovies(
    userId: Int,
    sessionId: String,
    sortBy: String,
  ): Flow<PagingData<MediaItem>>

  fun getFavoriteTv(
    userId: Int,
    sessionId: String,
    sortBy: String,
  ): Flow<PagingData<MediaItem>>
}
