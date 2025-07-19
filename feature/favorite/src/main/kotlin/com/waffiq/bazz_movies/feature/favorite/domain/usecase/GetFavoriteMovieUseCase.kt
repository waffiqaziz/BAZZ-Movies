package com.waffiq.bazz_movies.feature.favorite.domain.usecase

import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteMovieUseCase {
  fun getFavoriteMovies(sessionId: String): Flow<androidx.paging.PagingData<MediaItem>>
}
