package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Used for detail, favorite and watchlist module
 */
interface PostMethodUseCase {
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<NetworkResult<Post>>

  suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<NetworkResult<Post>>
}
