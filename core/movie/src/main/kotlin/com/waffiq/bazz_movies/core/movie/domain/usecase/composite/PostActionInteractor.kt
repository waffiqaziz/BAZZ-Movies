package com.waffiq.bazz_movies.core.movie.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostActionInteractor @Inject constructor(
  private val moviesRepository: IMoviesRepository,
  private val userRepository: IUserRepository,
) : PostActionUseCase {

  private suspend fun getUser() = userRepository.getUserPref().first()

  override fun postWatchlistWithAuth(wtc: WatchlistParams): Flow<Outcome<PostFavoriteWatchlist>> =
    flow {
      val user = getUser()
      emitAll(moviesRepository.postWatchlist(user.token, wtc, user.userId))
    }

  override fun postFavoriteWithAuth(fav: FavoriteParams): Flow<Outcome<PostFavoriteWatchlist>> =
    flow {
      val user = getUser()
      emitAll(moviesRepository.postFavorite(user.token, fav, user.userId))
    }
}
