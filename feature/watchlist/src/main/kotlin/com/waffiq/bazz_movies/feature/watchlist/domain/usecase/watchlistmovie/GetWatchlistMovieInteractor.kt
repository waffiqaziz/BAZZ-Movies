package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class GetWatchlistMovieInteractor @Inject constructor(
  private val watchlistRepository: IWatchlistRepository,
  private val userRepository: IUserRepository,
) : GetWatchlistMovieUseCase {

  override fun getWatchlistMovies(): Flow<PagingData<MediaItem>> =
    userRepository.getUserPref().flatMapConcat {
      watchlistRepository.getWatchlistMovies(it.userId, it.token)
    }
}
