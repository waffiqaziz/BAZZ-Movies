package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

/**
 * Used for detail, favorite and watchlist module
 */
interface PostMethodUseCase {
  fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<Post>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<Post>>
}
