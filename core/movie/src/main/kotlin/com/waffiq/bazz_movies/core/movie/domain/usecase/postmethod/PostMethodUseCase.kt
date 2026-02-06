package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

/**
 * Used for detail, favorite and watchlist module
 */
interface PostMethodUseCase {
  fun postFavorite(
    sessionId: String,
    fav: UpdateFavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postWatchlist(
    sessionId: String,
    wtc: UpdateWatchlistParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<PostResult>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<PostResult>>
}
