package com.waffiq.bazz_movies.domain.usecase.post_method

import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.domain.model.post.Post
import com.waffiq.bazz_movies.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PostMethodUseCase {
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlist>>

  suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<Post>>

  suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<Post>>
}
