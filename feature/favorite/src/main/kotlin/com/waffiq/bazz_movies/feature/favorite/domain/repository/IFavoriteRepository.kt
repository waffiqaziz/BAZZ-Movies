package com.waffiq.bazz_movies.feature.favorite.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
  fun getFavoriteMovies(userId: Int, sessionId: String): Flow<PagingData<MediaItem>>
  fun getFavoriteTv(userId: Int, sessionId: String): Flow<PagingData<MediaItem>>
}
