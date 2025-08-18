package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PostMethodWithUserInteractor @Inject constructor(
  private val postMethodUseCase: PostMethodUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : PostMethodWithUserUseCase {

  override suspend fun postFavorite(fav: FavoriteModel): Flow<Outcome<PostFavoriteWatchlist>> {
    userPrefUseCase.getUser().first().let {
      return postMethodUseCase.postFavorite(
        sessionId = it.token,
        fav = fav,
        userId = it.userId
      )
    }
  }

  override suspend fun postWatchlist(wtc: WatchlistModel): Flow<Outcome<PostFavoriteWatchlist>> {
    userPrefUseCase.getUser().first().let {
      return postMethodUseCase.postWatchlist(
        sessionId = it.token,
        wtc = wtc,
        userId = it.userId
      )
    }
  }

  override suspend fun postMovieRate(
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<Post>> {
    userPrefUseCase.getUser().first().let {
      return postMethodUseCase.postMovieRate(
        sessionId = it.token,
        rating = rating,
        movieId = movieId
      )
    }
  }

  override suspend fun postTvRate(
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<Post>> {
    userPrefUseCase.getUser().first().let {
      return postMethodUseCase.postTvRate(
        sessionId = it.token,
        rating = rating,
        tvId = tvId
      )
    }
  }
}
