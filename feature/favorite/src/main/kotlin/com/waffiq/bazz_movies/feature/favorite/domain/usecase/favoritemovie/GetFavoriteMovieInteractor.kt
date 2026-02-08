package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieInteractor @Inject constructor(
  private val favoriteRepository: IFavoriteRepository,
) : GetFavoriteMovieUseCase {
  override fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaItem>> =
    favoriteRepository.getFavoriteMovies(sessionId)
}
