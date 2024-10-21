package com.waffiq.bazz_movies.core.domain.usecase.post_method

import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostMethodInteractor @Inject constructor(
  private val postMethodRepository: IMoviesRepository
) : PostMethodUseCase {
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    postMethodRepository.postFavorite(sessionId, fav, userId)

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>> =
    postMethodRepository.postWatchlist(sessionId, wtc, userId)

  override suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<Post>> =
    postMethodRepository.postMovieRate(sessionId, data, movieId)

  override suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<Post>> =
    postMethodRepository.postTvRate(sessionId, data, tvId)
}