package com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

class GetWatchlistTvInteractor @Inject constructor(
  private val watchlistRepository: IWatchlistRepository,
  private val userRepository: IUserRepository,
) : GetWatchlistTvUseCase {
  override fun getWatchlistTv(): Flow<PagingData<MediaItem>> =
    userRepository.getUserPref().flatMapConcat {
      watchlistRepository.getWatchlistTv(it.userId, it.token)
    }
}
