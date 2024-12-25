package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import com.waffiq.bazz_movies.core.data.Post
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostMethodInteractor @Inject constructor(
  private val postMethodRepository: IMoviesRepository
) : PostMethodUseCase {
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    postMethodRepository.postFavorite(sessionId, fav, userId)

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    postMethodRepository.postWatchlist(sessionId, wtc, userId)

  override suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<NetworkResult<Post>> =
    postMethodRepository.postMovieRate(sessionId, rating, movieId)

  override suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<NetworkResult<Post>> =
    postMethodRepository.postTvRate(sessionId, rating, tvId)
}
