package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetFavoriteMovieUseCase {
  fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaItem>>
}
