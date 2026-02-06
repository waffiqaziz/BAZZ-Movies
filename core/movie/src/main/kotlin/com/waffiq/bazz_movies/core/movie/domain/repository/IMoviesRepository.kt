package com.waffiq.bazz_movies.core.movie.domain.repository

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>

  fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>

  // region POST FAVORITE AND WATCHLIST
  fun postFavorite(
    sessionId: String,
    fav: UpdateFavoriteParams,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postWatchlist(
    sessionId: String,
    wtc: UpdateWatchlistParams,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<Outcome<PostResult>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<Outcome<PostResult>>
  // endregion POST FAVORITE AND WATCHLIST
}
