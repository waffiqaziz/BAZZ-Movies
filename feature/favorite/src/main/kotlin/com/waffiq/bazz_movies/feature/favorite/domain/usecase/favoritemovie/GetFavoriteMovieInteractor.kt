package com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class GetFavoriteMovieInteractor @Inject constructor(
  private val favoriteRepository: IFavoriteRepository,
  private val userRepository: IUserRepository,
) : GetFavoriteMovieUseCase {

  override fun getFavoriteMovies(): Flow<PagingData<MediaItem>> =
    userRepository.getUserPref().flatMapConcat {
      favoriteRepository.getFavoriteMovies(it.userId, it.token)
    }
}
