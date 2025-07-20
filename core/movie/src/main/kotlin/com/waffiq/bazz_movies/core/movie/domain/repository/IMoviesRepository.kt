package com.waffiq.bazz_movies.core.movie.domain.repository

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>

  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<Outcome<Post>>

  suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<Outcome<Post>>
  // endregion POST FAVORITE AND WATCHLIST
}
