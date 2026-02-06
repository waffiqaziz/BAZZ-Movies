package com.waffiq.bazz_movies.core.movie.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostActionInteractor @Inject constructor(
  private val moviesRepository: IMoviesRepository,
  private val userRepository: IUserRepository,
) : PostActionUseCase {

  override fun postWatchlistWithAuth(
    wtc: UpdateWatchlistParams,
  ): Flow<Outcome<PostFavoriteWatchlist>> = flow {
    userRepository.getUserPref().collect { userPref ->
      moviesRepository.postWatchlist(
        sessionId = userPref.token,
        wtc = wtc,
        userId = userPref.userId
      ).collect { outcome ->
        emit(outcome)
      }
    }
  }

  override fun postFavoriteWithAuth(
    fav: UpdateFavoriteParams,
  ): Flow<Outcome<PostFavoriteWatchlist>> = flow {
    userRepository.getUserPref().collect { userPref ->
      moviesRepository.postFavorite(
        sessionId = userPref.token,
        fav = fav,
        userId = userPref.userId
      ).collect { outcome ->
        emit(outcome)
      }
    }
  }
}
